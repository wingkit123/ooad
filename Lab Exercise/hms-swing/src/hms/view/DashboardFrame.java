package hms.view;

import hms.controller.SystemController;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.Patient;
import hms.model.Role;
import hms.model.User;
import hms.service.DuplicateAppointmentException;
import hms.service.ReportSummary;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DashboardFrame extends JFrame {
    private final SystemController controller;
    private final User currentUser;

    private final DefaultTableModel patientModel = new DefaultTableModel(new String[]{"Select", "ID", "Name", "Age", "Gender", "History"}, 0) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Boolean.class;
            return super.getColumnClass(columnIndex);
        }
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final JTable patientTable = new JTable(patientModel);
    private final JTextField patientNameField = new JTextField(16);
    private final JTextField patientAgeField = new JTextField(5);
    private final JTextField patientGenderField = new JTextField(10);
    private final JTextField patientHistoryField = new JTextField(24);
    private int selectedPatientId = -1;

    private final JLabel patientNameLabel = new JLabel("Name");
    private final JLabel patientAgeLabel = new JLabel("Age");
    private final JLabel patientGenderLabel = new JLabel("Gender");
    private final JLabel patientHistoryLabel = new JLabel("History");
    private final JButton addPatientButton = new JButton("Add Patient");
    private final JButton updatePatientButton = new JButton("Update Patient");

    private final DefaultTableModel doctorModel = new DefaultTableModel(new String[]{"Select", "ID", "Name", "Specialization", "Status"}, 0) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Boolean.class;
            return super.getColumnClass(columnIndex);
        }
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final JTable doctorTable = new JTable(doctorModel);
    private final JTextField doctorNameField = new JTextField(16);
    private final JTextField specializationField = new JTextField(16);
    private final JComboBox<String> doctorStatusCombo = new JComboBox<>(new String[]{"Available", "On Leave", "Busy"});
    private int selectedDoctorId = -1;

    private final DefaultTableModel appointmentModel = new DefaultTableModel(new String[]{"Select", "ID", "Patient", "Doctor", "Date", "Time"}, 0) {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) return Boolean.class;
            return super.getColumnClass(columnIndex);
        }
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final JTable appointmentTable = new JTable(appointmentModel);
    private final JComboBox<Patient> patientCombo = new JComboBox<>();
    private final JComboBox<Doctor> doctorCombo = new JComboBox<>();
    private final JTextField dateField = new JTextField("2026-06-22", 10);
    private final JTextField timeField = new JTextField("10:00", 8);
    private int selectedAppointmentId = -1;

    private final JLabel doctorNameLabel = new JLabel("Name");
    private final JLabel specializationLabel = new JLabel("Specialization");

    private final JLabel patientComboLabel = new JLabel("Patient");
    private final JLabel doctorComboLabel = new JLabel("Doctor");
    private final JLabel dateFieldLabel = new JLabel("Date");
    private final JLabel timeFieldLabel = new JLabel("Time");
    private final JButton bookButton = new JButton("Book Appointment");
    private final JButton updateAppointmentButton = new JButton("Update Appointment");

    private final JPanel patientFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JPanel doctorFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JPanel appointmentFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    private final JTextArea reportArea = new JTextArea(12, 48);

    public DashboardFrame(SystemController controller, User currentUser) {
        super("Hospital Management System - Dashboard");
        this.controller = controller;
        this.currentUser = currentUser;
        buildUi();
        setupListeners();
        refreshAll();
    }

    private void buildUi() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JLabel header = new JLabel("Logged in as " + currentUser.getDisplayName() + " (" + currentUser.getRole() + ") - " + currentUser.getHomeMessage());
        headerPanel.add(header, BorderLayout.WEST);

        JButton signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener(event -> {
            dispose();
            new LoginFrame(controller).setVisible(true);
        });
        headerPanel.add(signOutButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", buildPatientPanel());
        tabs.addTab("Doctors", buildDoctorPanel());
        tabs.addTab("Appointments", buildAppointmentPanel());
        tabs.addTab("Reports", buildReportPanel());

        if (currentUser.getRole() == Role.DOCTOR) {
            tabs.setEnabledAt(1, false);
        }

        add(tabs, BorderLayout.CENTER);
    }

    private void setupListeners() {
        patientModel.addTableModelListener(event -> {
            if (event.getColumn() == 0) {
                updatePatientFormSelection();
            }
        });

        doctorModel.addTableModelListener(event -> {
            if (event.getColumn() == 0) {
                updateDoctorFormVisibility();
            }
        });

        appointmentModel.addTableModelListener(event -> {
            if (event.getColumn() == 0) {
                updateAppointmentFormSelection();
            }
        });
    }

    private JPanel buildPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                int row = patientTable.rowAtPoint(event.getPoint());
                if (row >= 0) {
                    Boolean val = (Boolean) patientModel.getValueAt(row, 0);
                    patientModel.setValueAt(!Boolean.TRUE.equals(val), row, 0);
                }
            }
        });
        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);

        patientTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        patientTable.getColumnModel().getColumn(0).setMaxWidth(60);

        JPanel form = new JPanel(new BorderLayout(6, 6));
        form.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));

        patientFieldsPanel.add(patientNameLabel);
        patientFieldsPanel.add(patientNameField);
        patientFieldsPanel.add(patientAgeLabel);
        patientFieldsPanel.add(patientAgeField);
        patientFieldsPanel.add(patientGenderLabel);
        patientFieldsPanel.add(patientGenderField);
        patientFieldsPanel.add(patientHistoryLabel);
        patientFieldsPanel.add(patientHistoryField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPatientButton.addActionListener(event -> addPatient());
        updatePatientButton.addActionListener(event -> updatePatient());
        JButton deleteButton = new JButton("Delete Patient");
        deleteButton.addActionListener(event -> deletePatients());

        buttonsPanel.add(addPatientButton);
        buttonsPanel.add(updatePatientButton);
        buttonsPanel.add(deleteButton);

        form.add(patientFieldsPanel, BorderLayout.CENTER);
        form.add(buttonsPanel, BorderLayout.SOUTH);

        if (currentUser.getRole() == Role.DOCTOR) {
            addPatientButton.setEnabled(false);
        }
        if (currentUser.getRole() != Role.ADMIN) {
            deleteButton.setEnabled(false);
        }

        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildDoctorPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                int row = doctorTable.rowAtPoint(event.getPoint());
                if (row >= 0) {
                    Boolean val = (Boolean) doctorModel.getValueAt(row, 0);
                    doctorModel.setValueAt(!Boolean.TRUE.equals(val), row, 0);
                }
            }
        });
        panel.add(new JScrollPane(doctorTable), BorderLayout.CENTER);

        doctorTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        doctorTable.getColumnModel().getColumn(0).setMaxWidth(60);

        JPanel form = new JPanel(new BorderLayout(6, 6));
        form.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));

        doctorFieldsPanel.add(doctorNameLabel);
        doctorFieldsPanel.add(doctorNameField);
        doctorFieldsPanel.add(specializationLabel);
        doctorFieldsPanel.add(specializationField);
        doctorFieldsPanel.add(new JLabel("Status"));
        doctorFieldsPanel.add(doctorStatusCombo);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Doctor");
        addButton.addActionListener(event -> addDoctor());
        JButton updateButton = new JButton("Update Doctor");
        updateButton.addActionListener(event -> updateDoctorDetailsAndStatus());
        JButton deleteButton = new JButton("Delete Doctor");
        deleteButton.addActionListener(event -> deleteDoctors());

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);

        form.add(doctorFieldsPanel, BorderLayout.CENTER);
        form.add(buttonsPanel, BorderLayout.SOUTH);

        if (currentUser.getRole() != Role.ADMIN) {
            addButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            doctorNameField.setEditable(false);
            specializationField.setEditable(false);
            doctorStatusCombo.setEnabled(false);
        }

        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildAppointmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        appointmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appointmentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                int row = appointmentTable.rowAtPoint(event.getPoint());
                if (row >= 0) {
                    Boolean val = (Boolean) appointmentModel.getValueAt(row, 0);
                    appointmentModel.setValueAt(!Boolean.TRUE.equals(val), row, 0);
                }
            }
        });
        panel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        appointmentTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        appointmentTable.getColumnModel().getColumn(0).setMaxWidth(60);

        JPanel form = new JPanel(new BorderLayout(6, 6));
        form.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6));

        appointmentFieldsPanel.add(patientComboLabel);
        appointmentFieldsPanel.add(patientCombo);
        appointmentFieldsPanel.add(doctorComboLabel);
        appointmentFieldsPanel.add(doctorCombo);
        appointmentFieldsPanel.add(dateFieldLabel);
        appointmentFieldsPanel.add(dateField);
        appointmentFieldsPanel.add(timeFieldLabel);
        appointmentFieldsPanel.add(timeField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bookButton.addActionListener(event -> bookAppointment());
        updateAppointmentButton.addActionListener(event -> updateAppointment());
        JButton deleteButton = new JButton("Delete Appointment");
        deleteButton.addActionListener(event -> deleteAppointments());

        buttonsPanel.add(bookButton);
        buttonsPanel.add(updateAppointmentButton);
        buttonsPanel.add(deleteButton);

        form.add(appointmentFieldsPanel, BorderLayout.CENTER);
        form.add(buttonsPanel, BorderLayout.SOUTH);

        if (currentUser.getRole() == Role.DOCTOR) {
            bookButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        reportArea.setEditable(false);
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        JButton refreshButton = new JButton("Generate Report");
        refreshButton.addActionListener(event -> refreshReport());
        actions.add(refreshButton, gbc);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private void addPatient() {
        try {
            int age;
            try {
                age = Integer.parseInt(patientAgeField.getText().trim());
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Patient age must be a valid positive integer (no letters allowed).");
            }
            controller.addPatient(
                    patientNameField.getText(),
                    age,
                    patientGenderField.getText(),
                    patientHistoryField.getText()
            );
            clearPatientForm();
            refreshAll();
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void updatePatient() {
        if (selectedPatientId < 0) {
            showError("Select a patient row first.");
            return;
        }
        try {
            int age;
            try {
                age = Integer.parseInt(patientAgeField.getText().trim());
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Patient age must be a valid positive integer (no letters allowed).");
            }
            controller.updatePatient(
                    selectedPatientId,
                    patientNameField.getText(),
                    age,
                    patientGenderField.getText(),
                    patientHistoryField.getText()
            );
            clearPatientForm();
            refreshAll();
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void addDoctor() {
        try {
            controller.addDoctor(doctorNameField.getText(), specializationField.getText());
            doctorNameField.setText("");
            specializationField.setText("");
            refreshAll();
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void bookAppointment() {
        Patient patient = (Patient) patientCombo.getSelectedItem();
        Doctor doctor = (Doctor) doctorCombo.getSelectedItem();
        if (patient == null || doctor == null) {
            showError("Add at least one patient and one doctor first.");
            return;
        }
        try {
            controller.bookAppointment(
                    patient.getId(),
                    doctor.getId(),
                    LocalDate.parse(dateField.getText().trim()),
                    LocalTime.parse(timeField.getText().trim())
            );
            refreshAll();
        } catch (DuplicateAppointmentException ex) {
            showError(ex.getMessage());
        } catch (DateTimeParseException ex) {
            showError("Use date format YYYY-MM-DD and time format HH:MM.");
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateAppointment() {
        if (selectedAppointmentId < 0) {
            showError("Select an appointment row first.");
            return;
        }
        Patient patient = (Patient) patientCombo.getSelectedItem();
        Doctor doctor = (Doctor) doctorCombo.getSelectedItem();
        if (patient == null || doctor == null) {
            showError("Add at least one patient and one doctor first.");
            return;
        }
        try {
            controller.updateAppointment(
                    selectedAppointmentId,
                    patient.getId(),
                    doctor.getId(),
                    LocalDate.parse(dateField.getText().trim()),
                    LocalTime.parse(timeField.getText().trim())
            );
            selectedAppointmentId = -1;
            refreshAll();
        } catch (DuplicateAppointmentException ex) {
            showError(ex.getMessage());
        } catch (DateTimeParseException ex) {
            showError("Use date format YYYY-MM-DD and time format HH:MM.");
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteAppointments() {
        List<Integer> idsToDelete = new ArrayList<>();
        for (int i = 0; i < appointmentModel.getRowCount(); i++) {
            Boolean selected = (Boolean) appointmentModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selected)) {
                idsToDelete.add((Integer) appointmentModel.getValueAt(i, 1));
            }
        }
        if (idsToDelete.isEmpty()) {
            if (selectedAppointmentId >= 0) {
                idsToDelete.add(selectedAppointmentId);
            } else {
                showError("Please tick/select at least one appointment to delete.");
                return;
            }
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the " + idsToDelete.size() + " selected appointment(s)?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                for (int id : idsToDelete) {
                    controller.deleteAppointment(id);
                }
                selectedAppointmentId = -1;
                refreshAll();
            } catch (RuntimeException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void updateDoctorFormVisibility() {
        int checkedCount = 0;
        int lastCheckedId = -1;
        String name = "";
        String spec = "";
        String status = "Available";

        for (int i = 0; i < doctorModel.getRowCount(); i++) {
            Boolean selected = (Boolean) doctorModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selected)) {
                checkedCount++;
                lastCheckedId = (int) doctorModel.getValueAt(i, 1);
                name = (String) doctorModel.getValueAt(i, 2);
                spec = (String) doctorModel.getValueAt(i, 3);
                status = (String) doctorModel.getValueAt(i, 4);
            }
        }

        if (checkedCount > 1) {
            doctorNameLabel.setVisible(false);
            doctorNameField.setVisible(false);
            specializationLabel.setVisible(false);
            specializationField.setVisible(false);
            selectedDoctorId = -1;
        } else {
            doctorNameLabel.setVisible(true);
            doctorNameField.setVisible(true);
            specializationLabel.setVisible(true);
            specializationField.setVisible(true);

            if (checkedCount == 1) {
                selectedDoctorId = lastCheckedId;
                doctorNameField.setText(name);
                specializationField.setText(spec);
                doctorStatusCombo.setSelectedItem(status);
            } else {
                selectedDoctorId = -1;
                doctorNameField.setText("");
                specializationField.setText("");
            }
        }

        if (doctorFieldsPanel.getParent() != null) {
            doctorFieldsPanel.getParent().revalidate();
            doctorFieldsPanel.getParent().repaint();
        }
    }

    private void updateAppointmentFormSelection() {
        int checkedCount = 0;
        int lastCheckedId = -1;

        for (int i = 0; i < appointmentModel.getRowCount(); i++) {
            Boolean selected = (Boolean) appointmentModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selected)) {
                checkedCount++;
                lastCheckedId = (int) appointmentModel.getValueAt(i, 1);
            }
        }

        boolean singleSelected = (checkedCount == 1);
        boolean visible = (checkedCount <= 1);

        appointmentFieldsPanel.setVisible(visible);
        bookButton.setVisible(visible);
        updateAppointmentButton.setVisible(visible);

        if (singleSelected) {
            selectedAppointmentId = lastCheckedId;
            Appointment app = controller.getAppointments().stream()
                    .filter(a -> a.getId() == selectedAppointmentId)
                    .findFirst().orElse(null);
            if (app != null) {
                for (int i = 0; i < patientCombo.getItemCount(); i++) {
                    if (patientCombo.getItemAt(i).getId() == app.getPatient().getId()) {
                        patientCombo.setSelectedIndex(i);
                        break;
                    }
                }
                for (int i = 0; i < doctorCombo.getItemCount(); i++) {
                    if (doctorCombo.getItemAt(i).getId() == app.getDoctor().getId()) {
                        doctorCombo.setSelectedIndex(i);
                        break;
                    }
                }
                dateField.setText(app.getDate().toString());
                timeField.setText(app.getTime().toString());
            }
        } else {
            selectedAppointmentId = -1;
            dateField.setText("2026-06-22");
            timeField.setText("10:00");
        }

        if (appointmentFieldsPanel.getParent() != null) {
            appointmentFieldsPanel.getParent().revalidate();
            appointmentFieldsPanel.getParent().repaint();
        }
    }

    private void updatePatientFormSelection() {
        int checkedCount = 0;
        int lastCheckedId = -1;
        String name = "";
        String age = "";
        String gender = "";
        String history = "";

        for (int i = 0; i < patientModel.getRowCount(); i++) {
            Boolean selected = (Boolean) patientModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selected)) {
                checkedCount++;
                lastCheckedId = (int) patientModel.getValueAt(i, 1);
                name = (String) patientModel.getValueAt(i, 2);
                age = String.valueOf(patientModel.getValueAt(i, 3));
                gender = (String) patientModel.getValueAt(i, 4);
                history = (String) patientModel.getValueAt(i, 5);
            }
        }

        boolean visible = (checkedCount <= 1);
        patientFieldsPanel.setVisible(visible);
        addPatientButton.setVisible(visible);
        updatePatientButton.setVisible(visible);

        if (checkedCount == 1) {
            selectedPatientId = lastCheckedId;
            patientNameField.setText(name);
            patientAgeField.setText(age);
            patientGenderField.setText(gender);
            patientHistoryField.setText(history);
        } else {
            selectedPatientId = -1;
            patientNameField.setText("");
            patientAgeField.setText("");
            patientGenderField.setText("");
            patientHistoryField.setText("");
        }

        if (patientFieldsPanel.getParent() != null) {
            patientFieldsPanel.getParent().revalidate();
            patientFieldsPanel.getParent().repaint();
        }
    }

    private void deletePatients() {
        List<Integer> idsToDelete = new ArrayList<>();
        for (int i = 0; i < patientModel.getRowCount(); i++) {
            Boolean selected = (Boolean) patientModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selected)) {
                idsToDelete.add((Integer) patientModel.getValueAt(i, 1));
            }
        }
        if (idsToDelete.isEmpty()) {
            if (selectedPatientId >= 0) {
                idsToDelete.add(selectedPatientId);
            } else {
                showError("Please tick/select at least one patient to delete.");
                return;
            }
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the " + idsToDelete.size() + " selected patient(s)? This will also delete their appointments.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                for (int id : idsToDelete) {
                    controller.deletePatient(id);
                }
                clearPatientForm();
                refreshAll();
            } catch (RuntimeException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void deleteDoctors() {
        List<Integer> idsToDelete = new ArrayList<>();
        for (int i = 0; i < doctorModel.getRowCount(); i++) {
            Boolean selected = (Boolean) doctorModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selected)) {
                idsToDelete.add((Integer) doctorModel.getValueAt(i, 1));
            }
        }
        if (idsToDelete.isEmpty()) {
            if (selectedDoctorId >= 0) {
                idsToDelete.add(selectedDoctorId);
            } else {
                showError("Please tick/select at least one doctor to delete.");
                return;
            }
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the " + idsToDelete.size() + " selected doctor(s)? This will also delete their appointments.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                for (int id : idsToDelete) {
                    controller.deleteDoctor(id);
                }
                doctorNameField.setText("");
                specializationField.setText("");
                selectedDoctorId = -1;
                refreshAll();
            } catch (RuntimeException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void updateDoctorDetailsAndStatus() {
        List<Integer> selectedIds = new ArrayList<>();
        for (int i = 0; i < doctorModel.getRowCount(); i++) {
            Boolean selected = (Boolean) doctorModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selected)) {
                selectedIds.add((Integer) doctorModel.getValueAt(i, 1));
            }
        }
        if (selectedIds.isEmpty()) {
            if (selectedDoctorId >= 0) {
                selectedIds.add(selectedDoctorId);
            } else {
                showError("Please select/tick at least one doctor.");
                return;
            }
        }

        try {
            String newStatus = (String) doctorStatusCombo.getSelectedItem();
            if (selectedIds.size() == 1) {
                int docId = selectedIds.get(0);
                controller.updateDoctor(docId, doctorNameField.getText(), specializationField.getText(), newStatus);
            } else {
                for (int docId : selectedIds) {
                    Doctor doc = controller.getDoctors().stream()
                            .filter(d -> d.getId() == docId)
                            .findFirst().orElse(null);
                    if (doc != null) {
                        controller.updateDoctor(docId, doc.getName(), doc.getSpecialization(), newStatus);
                    }
                }
            }
            doctorNameField.setText("");
            specializationField.setText("");
            selectedDoctorId = -1;
            refreshAll();
        } catch (RuntimeException ex) {
            showError(ex.getMessage());
        }
    }

    private void refreshAll() {
        refreshPatients();
        refreshDoctors();
        refreshAppointments();
        refreshReport();
    }

    private void refreshPatients() {
        patientModel.setRowCount(0);
        patientCombo.removeAllItems();
        for (Patient patient : controller.getPatients()) {
            patientModel.addRow(new Object[]{
                    Boolean.FALSE,
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getMedicalHistory()
            });
            patientCombo.addItem(patient);
        }
    }

    private void refreshDoctors() {
        doctorModel.setRowCount(0);
        doctorCombo.removeAllItems();
        for (Doctor doctor : controller.getDoctors()) {
            doctorModel.addRow(new Object[]{
                    Boolean.FALSE,
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getStatus()
            });
            doctorCombo.addItem(doctor);
        }
    }

    private void refreshAppointments() {
        appointmentModel.setRowCount(0);
        for (Appointment appointment : controller.getAppointments()) {
            appointmentModel.addRow(new Object[]{
                    Boolean.FALSE,
                    appointment.getId(),
                    appointment.getPatient().getName(),
                    appointment.getDoctor().getName(),
                    appointment.getDate(),
                    appointment.getTime()
            });
        }
    }

    private void refreshReport() {
        ReportSummary summary = controller.generateReport();
        StringBuilder builder = new StringBuilder();
        builder.append("Total Patients: ").append(summary.getTotalPatients()).append('\n');
        builder.append("Total Appointments: ").append(summary.getTotalAppointments()).append('\n');
        builder.append('\n').append("Doctor Schedules").append('\n');
        if (summary.getDoctorScheduleLines().isEmpty()) {
            builder.append("No appointments booked.");
        } else {
            for (String line : summary.getDoctorScheduleLines()) {
                builder.append("- ").append(line).append('\n');
            }
        }
        reportArea.setText(builder.toString());
    }

    private void clearPatientForm() {
        selectedPatientId = -1;
        patientNameField.setText("");
        patientAgeField.setText("");
        patientGenderField.setText("");
        patientHistoryField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
