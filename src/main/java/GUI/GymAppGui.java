/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author camilocardonasuarez
 */
import com.gym.modelo.RepositorioUsuarios;
import com.gym.modelo.Usuario;
import com.gym.modelo.Usuario1Mes;
import com.gym.modelo.Usuario3Meses;
import com.gym.modelo.Usuario6Meses;
import controlador.EjerciciosAPI;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.io.File;
import java.util.Date;
import java.util.List;

public class GymAppGui extends JFrame {
    private final JTextField txtNombre = new JTextField();
    private final JComboBox<String> cbPlan = new JComboBox<>(new String[]{"1 mes", "3 meses", "6 meses"});
    private final JTable tabla = new JTable();
    private final JSpinner spFechaPago = new JSpinner(new SpinnerDateModel());

    private final RepositorioUsuarios repo;
    private List<Usuario> vistaActual;

    public GymAppGui() {
        super("Gestion Gimnasio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JSpinner.DateEditor editor = new JSpinner.DateEditor(spFechaPago, "yyyy-MM-dd");
        spFechaPago.setEditor(editor);
        spFechaPago.setValue(new Date());

        File data = new File(System.getProperty("user.home"), "usuarios_gym.csv");
        repo = new RepositorioUsuarios(data);

        JPanel pnl = new JPanel(new BorderLayout(10, 10));
        pnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl.add(crearPanelRegistro(), BorderLayout.NORTH);
        pnl.add(new JScrollPane(tabla), BorderLayout.CENTER);
        pnl.add(crearPanelBusqueda(), BorderLayout.SOUTH);
        setContentPane(pnl);

        configurarTabla();
        refrescarTabla(repo.listarTodos());
    }

    private JPanel crearPanelRegistro() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(this::registrarUsuario);

        JButton btnAPI = new JButton("Ejemplos de Ejercicios");
        btnAPI.addActionListener(e -> cargarEjercicios());

        JButton btnCambiarFecha = new JButton("Cambiar fecha pago (selección)");
        btnCambiarFecha.addActionListener(e -> cambiarFechaSeleccion());

        JButton btnCambiarNombre = new JButton("Cambiar nombre (selección)");
        btnCambiarNombre.addActionListener(e -> cambiarNombreSeleccion());

        JButton btnCambiarPlan = new JButton("Cambiar plan (selección)");
        btnCambiarPlan.addActionListener(e -> cambiarPlanSeleccion());

        int row = 0;

        c.gridx = 0; c.gridy = row;
        p.add(new JLabel("Nombre:"), c);

        c.gridx = 1; c.gridy = row; c.weightx = 1;
        p.add(txtNombre, c);
        c.weightx = 0;

        row++;
        c.gridx = 0; c.gridy = row;
        p.add(new JLabel("Plan:"), c);

        c.gridx = 1; c.gridy = row;
        p.add(cbPlan, c);

        row++;
        c.gridx = 0; c.gridy = row;
        p.add(new JLabel("Fecha pago:"), c);

        c.gridx = 1; c.gridy = row;
        p.add(spFechaPago, c);

        row++;
        c.gridx = 0; c.gridy = row;
        p.add(btnRegistrar, c);

        c.gridx = 1; c.gridy = row;
        p.add(btnAPI, c);

        c.gridx = 2; c.gridy = row;
        p.add(btnCambiarFecha, c);

        c.gridx = 3; c.gridy = row;
        p.add(btnCambiarNombre, c);

        c.gridx = 4; c.gridy = row;
        p.add(btnCambiarPlan, c);

        return p;
    }

    private JPanel crearPanelBusqueda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        return p;
    }

    private void configurarTabla() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Nombre", "Plan (meses)", "Fecha pago", "Fecha vencimiento", "Estado", "Precio"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla.setModel(model);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void refrescarTabla(List<Usuario> usuarios) {
        this.vistaActual = usuarios;
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        m.setRowCount(0);
        LocalDate hoy = LocalDate.now();
        for (Usuario u : usuarios) {
            m.addRow(new Object[]{
                    u.getNombre(),
                    u.getMesesDuracion(),
                    u.getFechaPagoStr(),
                    u.getFechaVencimientoStr(),
                    u.getEstado(hoy),
                    String.format("$%,.0f", u.calcularPrecio())
            });
        }
    }

    private LocalDate obtenerFechaDesdeSpinner(JSpinner spinner) {
        Date d = (Date) spinner.getValue();
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void registrarUsuario(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el nombre.", "Validacion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idx = cbPlan.getSelectedIndex();
        LocalDate fechaSeleccionada = obtenerFechaDesdeSpinner(spFechaPago);

        Usuario u;
        u = switch (idx) {
            case 0 -> new Usuario1Mes(nombre, fechaSeleccionada);
            case 1 -> new Usuario3Meses(nombre, fechaSeleccionada);
            case 2 -> new Usuario6Meses(nombre, fechaSeleccionada);
            default -> new Usuario1Mes(nombre, fechaSeleccionada);
        };

        repo.agregar(u);
        JOptionPane.showMessageDialog(this, "Registrado: " + u, "OK", JOptionPane.INFORMATION_MESSAGE);

        txtNombre.setText("");
        spFechaPago.setValue(new Date());
        refrescarTabla(repo.listarTodos());
    }

    private void cargarEjercicios() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            String raw;
            List<String> nombres;

            @Override
            protected Void doInBackground() {
                try {
                    raw = EjerciciosAPI.getRaw();
                    nombres = EjerciciosAPI.extraerNombres(raw);
                } catch (Exception ex) {
                    raw = "Error: " + ex.getMessage();
                }
                return null;
            }

            @Override
            protected void done() {
                if (nombres != null && !nombres.isEmpty()) {
                    JTextArea area = new JTextArea();
                    area.setEditable(false);

                    StringBuilder sb = new StringBuilder("Algunos ejercicios desde la API:\n\n");
                    for (String n : nombres) sb.append("• ").append(n).append("\n");

                    area.setText(sb.toString());

                    JOptionPane.showMessageDialog(
                            GymAppGui.this,
                            new JScrollPane(area),
                            "API wger",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            GymAppGui.this,
                            raw,
                            "API wger (raw)",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        };
        worker.execute();
    }

    private void cambiarFechaSeleccion() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = vistaActual.get(row);

        JSpinner sp = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor ed = new JSpinner.DateEditor(sp, "yyyy-MM-dd");
        sp.setEditor(ed);

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(u.getFechaPago().getYear(), u.getFechaPago().getMonthValue() - 1, u.getFechaPago().getDayOfMonth());
        sp.setValue(cal.getTime());

        int op = JOptionPane.showConfirmDialog(this, sp,
                "Nueva fecha de pago para " + u.getNombre(),
                JOptionPane.OK_CANCEL_OPTION);

        if (op == JOptionPane.OK_OPTION) {
            Date d = (Date) sp.getValue();
            LocalDate nueva = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            u.setFechaPago(nueva);
            repo.persistirCambios();
            refrescarTabla(repo.listarTodos());
        }
    }

    private void cambiarNombreSeleccion() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = vistaActual.get(row);

        String nuevo = JOptionPane.showInputDialog(this, "Nuevo nombre:", u.getNombre());
        if (nuevo != null && !nuevo.trim().isEmpty()) {
            u.setNombre(nuevo.trim());
            repo.persistirCambios();
            refrescarTabla(repo.listarTodos());
        }
    }

    private void cambiarPlanSeleccion() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un usuario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario u = vistaActual.get(row);

        String[] opciones = {"1 mes", "3 meses", "6 meses"};
        String sel = (String) JOptionPane.showInputDialog(
                this, "Nuevo plan para " + u.getNombre(),
                "Cambiar plan", JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        if (sel == null) return;

        int meses = sel.startsWith("1") ? 1 : sel.startsWith("3") ? 3 : 6;
        LocalDate fecha = u.getFechaPago();
        String nombre = u.getNombre();

        Usuario nuevo;
        nuevo = switch (meses) {
            case 1 -> new Usuario1Mes(nombre, fecha);
            case 3 -> new Usuario3Meses(nombre, fecha);
            case 6 -> new Usuario6Meses(nombre, fecha);
            default -> new Usuario1Mes(nombre, fecha);
        };

        repo.reemplazar(u, nuevo);
        refrescarTabla(repo.listarTodos());
    }
}

    
