Java AES File Encryption Tool 

This project is a secure desktop-based AES file encryption/decryption tool using Java Swing. It allows users to encrypt and decrypt any file (e.g., PDFs, images, documents) with AES encryption (CBC mode).

Contents:
- AESUtil.java: AES logic (generate key, IV, encrypt, decrypt)
- FileEncryptorGUI.java: GUI tool to browse, encrypt, decrypt files
- aes.key: Auto-generated encryption key file used during decryption

How to Run:
1. Compile both Java files:
   javac AESUtil.java FileEncryptorGUI.java

2. Run the tool:
   java FileEncryptorGUI

Features:
- 128-bit AES encryption using CBC mode
- Encrypt any file type and save securely
- Automatically handles IV and key file
- Simple and user-friendly interface
