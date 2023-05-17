import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFEmailExtractorGUI {

    private JFrame frame;
    private JTextField pdfFileField;
    private JTextField outputFileField;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PDFEmailExtractorGUI window = new PDFEmailExtractorGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public PDFEmailExtractorGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("PDF Email Extractor");

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel pdfFileLabel = new JLabel("PDF Dosyası:");
        pdfFileLabel.setBounds(10, 28, 100, 14);
        panel.add(pdfFileLabel);

        pdfFileField = new JTextField();
        pdfFileField.setBounds(120, 25, 200, 20);
        panel.add(pdfFileField);
        pdfFileField.setColumns(10);

        JButton pdfFileButton = new JButton("Seç");
        pdfFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("PDF dosyaları", "pdf"));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    pdfFileField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        pdfFileButton.setBounds(330, 25, 89, 23);
        panel.add(pdfFileButton);

        JLabel outputFileLabel = new JLabel("Çıktı Dosyası:");
        outputFileLabel.setBounds(10, 73, 100, 14);
        panel.add(outputFileLabel);

        outputFileField = new JTextField();
        outputFileField.setBounds(120, 70, 200, 20);
        panel.add(outputFileField);
        outputFileField.setColumns(10);

        JButton outputFileButton = new JButton("Seç");
        outputFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text dosyaları", "txt"));
                int result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    if (!filePath.endsWith(".txt")) {
                        filePath += ".txt";
                    }
                    outputFileField.setText(filePath);
                }
            }
        });
        outputFileButton.setBounds(330, 70, 89, 23);
        panel.add(outputFileButton);

        JButton extractButton = new JButton("Çıkart");
        extractButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pdfFilePath = pdfFileField.getText();
                String outputFilePath = outputFileField.getText();
                if (!pdfFilePath.isEmpty() && !outputFilePath.isEmpty()) {
                    extractEmails(pdfFilePath, outputFilePath);
                }
            }
        });
        extractButton.setBounds(165, 120, 89, 23);
        panel.add(extractButton);

        JLabel titleLabel = new JLabel("PDF Email Extractor");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(10, 0, 414, 14);
        panel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        JButton closeButton = new JButton("Kapat");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(closeButton);
    }

    private void extractEmails(String pdfFilePath, String outputFilePath) {
        try {

            PDDocument document = PDDocument.load(new File(pdfFilePath));


            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);


            Pattern pattern = Pattern.compile("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b");
            Matcher matcher = pattern.matcher(text);


            File file = new File(outputFilePath);
            FileWriter writer = new FileWriter(file);
            while (matcher.find()) {
                writer.write(matcher.group() + "\n");
            }
            writer.close();

            document.close();

            System.out.println("E-posta adresleri başarıyla dosyaya yazıldı.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}