# Remote File Lister

The **Remote File Lister** is a desktop utility built using Jetpack Compose and JetBrains Jewel. It allows users to browse files in a remote or local directory with lazy loading.

---

## Features
- **File Browsing**:
    - View files and directories in a tree structure.
    - Expand and collapse directories to explore nested files.
    - Support for multiple file selection.

- **Lazy Loading**:
    - Implements one-way lazy loading during scrolling (forward scrolling only).
    - **Note**: Bidirectional lazy loading (backward scrolling) is **not supported**.

- **File Info Popup**:
    - Displays detailed information about a selected file in a popup.

- **Status Bar**:
    - A status bar shows details about the currently selected file or directory.

- **RPC Implementation**:
    - For simplicity, the application includes a local `FileService` RPC implementation that mimics a remote service while operating on local files.


---

## How to Run the Application

After build just run
  ```bash
   ./gradlew run -Ddir=<directory_path>
  ```
`directory_path` is used when you want to operate on local file system. Specify anything from your `user.home` folder.

