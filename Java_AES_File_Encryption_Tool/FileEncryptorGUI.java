import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

public class FileEncryptorGUI extends JFrame {
    private JTextField fileField;

    public FileEncryptorGUI() {
        setTitle("AES File Encryption Tool");
        setSize(500, 160);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fileField = new JTextField(25);
        JButton browseBtn = new JButton("Browse");
        JButton encryptBtn = new JButton("Encrypt");
        JButton decryptBtn = new JButton("Decrypt");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select File:"));
        panel.add(fileField);
        panel.add(browseBtn);
        panel.add(encryptBtn);
        panel.add(decryptBtn);
        add(panel);

        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showOpenDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                fileField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        encryptBtn.addActionListener(e -> encryptFile());
        decryptBtn.addActionListener(e -> decryptFile());
    }

    private void encryptFile() {
        try {
            File file = new File(fileField.getText());
            byte[] data = Files.readAllBytes(file.toPath());

            SecretKey key = AESUtil.generateKey(128);
            AESUtil.saveKey(key, "aes.key");

            IvParameterSpec iv = AESUtil.generateIv();
            byte[] encrypted = AESUtil.encrypt(data, key, iv);

            File outFile = new File(file.getParent(), file.getName() + ".aes");
            try (FileOutputStream fos = new FileOutputStream(outFile)) {
                fos.write(iv.getIV()); // prepend IV
                fos.write(encrypted);
            }

            JOptionPane.showMessageDialog(this, "Encrypted file saved: " + outFile.getName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Encryption failed: " + e.getMessage());
        }
    }

    private void decryptFile() {
        try {
            File file = new File(fileField.getText());
            byte[] input = Files.readAllBytes(file.toPath());

            if (input.length <= 16) {
                JOptionPane.showMessageDialog(this, "File too short or invalid.");
                return;
            }

            byte[] ivBytes = Arrays.copyOfRange(input, 0, 16);
            byte[] encrypted = Arrays.copyOfRange(input, 16, input.length);

            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKey key = AESUtil.loadKey("aes.key");

            byte[] decrypted = AESUtil.decrypt(encrypted, key, iv);

            String originalName = file.getName().replace(".aes", "");
            File outFile = new File(file.getParent(), "decrypted_" + originalName);
            Files.write(outFile.toPath(), decrypted);

            JOptionPane.showMessageDialog(this, "Decrypted file saved: " + outFile.getName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Decryption failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileEncryptorGUI().setVisible(true));
    }
}