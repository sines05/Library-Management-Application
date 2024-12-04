/*package lk.ijse.bookWormLibraryManagementSystem.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyEvent;
import lk.ijse.bookWormLibraryManagementSystem.dto.BookDto;
import lk.ijse.bookWormLibraryManagementSystem.service.ServiceFactory;
import lk.ijse.bookWormLibraryManagementSystem.service.custom.BookService;
import lk.ijse.bookWormLibraryManagementSystem.util.Navigation;
import lk.ijse.bookWormLibraryManagementSystem.util.RegExPatterns;
import lk.ijse.bookWormLibraryManagementSystem.util.StyleUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddBookPopUpFormController {

    @FXML
    private Pane addPane;

    @FXML
    private Pane cancelPane;

    @FXML
    private Pane closePane;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblAdd;

    @FXML
    private Label lblCancel;

    @FXML
    private Label lblLanguageAlert;

    @FXML
    private Label lblNameAlert;

    @FXML
    private Label lblTypeAlert;

    @FXML
    private TextField txtLanguage;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtType;

    BookService bookService =
            (BookService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceTypes.BOOK);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateBook()) {
            BookDto bookDto = new BookDto();
            bookDto.setName(txtName.getText());
            bookDto.setLanguage(txtLanguage.getText());
            bookDto.setType(txtType.getText());
            bookDto.setAdmin(AdminSignInFormController.admin);
            bookDto.setStatus("Available");

            if (bookService.saveBook(bookDto)) {
                Navigation.closePopUpPane();
                AdminBookManagementFormController.getInstance().allBookId();
            } else {
                System.out.println("Unable to save book!");
            }
        }
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        String keyword = txtName.getText().trim();
        if (!keyword.isEmpty()) {
            try {
                // Gửi yêu cầu đến Google Books API
                String jsonResponse = fetchBookDataFromGoogleBooks(keyword);

                // Phân tích dữ liệu JSON và điền thông tin vào các trường
                Map<String, String> bookDetails = parseBookJSON(jsonResponse);

                // Điền thông tin sách vào các trường
                txtName.setText(bookDetails.get("title"));
                txtLanguage.setText(bookDetails.get("language"));
                txtType.setText(bookDetails.get("type"));

                showInfoAlert("Sách tìm thấy: " + bookDetails.get("title"));
            } catch (Exception e) {
                showErrorAlert("Lỗi khi tìm kiếm sách: " + e.getMessage());
            }
        } else {
            showErrorAlert("Tên sách không được để trống!");
        }
    }

    private String fetchBookDataFromGoogleBooks(String keyword) throws Exception {
        String query = keyword.replace(" ", "+");
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=AIzaSyA0hf18hdyylYo_lTEG1jqY1ho0hXE_q5U";
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            reader.close();
            return jsonResponse.toString();
        } else {
            throw new Exception("Không thể kết nối với API. Mã lỗi: " + responseCode);
        }
    }

    private Map<String, String> parseBookJSON(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray items = jsonObject.optJSONArray("items");
        if (items != null && items.length() > 0) {
            JSONObject firstBook = items.getJSONObject(0).getJSONObject("volumeInfo");
            String title = firstBook.optString("title", "Không có tên sách");
            String language = firstBook.optString("language", "Không có ngôn ngữ");
            String type = firstBook.optJSONArray("categories") != null ?
                    firstBook.optJSONArray("categories").getString(0) : "Không có loại sách";

            Map<String, String> bookDetails = new HashMap<>();
            bookDetails.put("title", title);
            bookDetails.put("language", language);
            bookDetails.put("type", type);

            return bookDetails;
        } else {
            throw new Exception("Không tìm thấy sách với từ khóa đã nhập.");
        }
    }

    private boolean validateBook() {
        boolean isValid = true;

        if (RegExPatterns.namePattern(txtName.getText())) {
            lblNameAlert.setText("Tên không hợp lệ!");
            isValid = false;
        }

        if (RegExPatterns.namePattern(txtLanguage.getText())) {
            lblLanguageAlert.setText("Ngôn ngữ không hợp lệ!");
            isValid = false;
        }

        if (RegExPatterns.namePattern(txtType.getText())) {
            lblTypeAlert.setText("Loại sách không hợp lệ!");
            isValid = false;
        }
        return isValid;
    }

    private void showInfoAlert(String message) {
        System.out.println("INFO: " + message); // Có thể thay bằng Alert hoặc Log
    }

    private void showErrorAlert(String message) {
        System.out.println("ERROR: " + message); // Có thể thay bằng Alert
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnAddOnMouseEntered(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnSelected(addPane, lblAdd);
    }

    @FXML
    void btnAddOnMouseExited(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnUnselected(addPane, lblAdd);
    }

    @FXML
    void btnCancelOnMouseEntered(MouseEvent event) {
        StyleUtil.cancelBtnSelected(cancelPane, lblCancel);
    }

    @FXML
    void btnCancelOnMouseExited(MouseEvent event) {
        StyleUtil.cancelBtnUnselected(cancelPane, lblCancel);
    }

    @FXML
    void btnCloseOnMouseEntered(MouseEvent event) {
        StyleUtil.closeIconBtnSelected(closePane, imgClose);
    }

    @FXML
    void btnCloseOnMouseExited(MouseEvent event) {
        StyleUtil.closeIconBtnUnselected(closePane, imgClose);
    }

    @FXML
    void txtLanguageOnAction(ActionEvent event) {
        txtType.requestFocus();
    }



    @FXML
    void txtTypeOnAction(ActionEvent event) {
        btnAddOnAction(event);
    }

    @FXML
    void txtNameOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtName.getText())) {
            lblNameAlert.setText("Invalid Name!!");
        } else lblNameAlert.setText(" ");
    }

    @FXML
    void txtLanguageOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtLanguage.getText())) {
            lblLanguageAlert.setText("Invalid Language!!");
        } else lblLanguageAlert.setText(" ");
    }

    @FXML
    void txtTypeOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtType.getText())) {
            lblTypeAlert.setText("Invalid Book Type!!");
        } else lblTypeAlert.setText(" ");
    }
}
*/
/*package lk.ijse.bookWormLibraryManagementSystem.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lk.ijse.bookWormLibraryManagementSystem.dto.BookDto;
import lk.ijse.bookWormLibraryManagementSystem.service.ServiceFactory;
import lk.ijse.bookWormLibraryManagementSystem.service.custom.BookService;
import lk.ijse.bookWormLibraryManagementSystem.util.Navigation;
import lk.ijse.bookWormLibraryManagementSystem.util.RegExPatterns;
import lk.ijse.bookWormLibraryManagementSystem.util.StyleUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddBookPopUpFormController {

    @FXML
    private Pane addPane;

    @FXML
    private Pane cancelPane;

    @FXML
    private Pane closePane;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblAdd;

    @FXML
    private Label lblCancel;

    @FXML
    private Label lblLanguageAlert;

    @FXML
    private Label lblNameAlert;

    @FXML
    private Label lblTypeAlert;

    @FXML
    private TextField txtLanguage;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtType;

    @FXML
    private ListView<String> lstBooks;  // ListView để hiển thị danh sách sách tìm được

    private ObservableList<String> booksList = FXCollections.observableArrayList();

    BookService bookService =
            (BookService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceTypes.BOOK);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateBook()) {
            BookDto bookDto = new BookDto();
            bookDto.setName(txtName.getText());
            bookDto.setLanguage(txtLanguage.getText());
            bookDto.setType(txtType.getText());
            bookDto.setAdmin(AdminSignInFormController.admin);
            bookDto.setStatus("Available");

            if (bookService.saveBook(bookDto)) {
                Navigation.closePopUpPane();
                AdminBookManagementFormController.getInstance().allBookId();
            } else {
                System.out.println("Unable to save book!");
            }
        }
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        String keyword = txtName.getText().trim();
        if (!keyword.isEmpty()) {
            try {
                // Gửi yêu cầu đến Google Books API
                String jsonResponse = fetchBookDataFromGoogleBooks(keyword);

                // Phân tích dữ liệu JSON và điền thông tin vào các trường
                Map<String, String> bookDetails = parseBookJSON(jsonResponse);

                // Cập nhật ListView
                updateBooksList(bookDetails);

            } catch (Exception e) {
                showErrorAlert("Lỗi khi tìm kiếm sách: " + e.getMessage());
            }
        } else {
            showErrorAlert("Tên sách không được để trống!");
        }
    }

    private void updateBooksList(Map<String, String> bookDetails) {
        // Cập nhật ListView với danh sách sách
        booksList.clear();  // Xóa danh sách cũ
        booksList.add(bookDetails.get("title"));  // Thêm sách tìm thấy vào danh sách
        lstBooks.setItems(booksList);  // Hiển thị danh sách vào ListView
    }

    @FXML
    void onBookSelected(MouseEvent event) {
        String selectedBook = lstBooks.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            txtName.setText(selectedBook);  // Điền tên sách vào TextField tên sách
        }
    }

    private String fetchBookDataFromGoogleBooks(String keyword) throws Exception {
        String query = keyword.replace(" ", "+");
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=AIzaSyA0hf18hdyylYo_lTEG1jqY1ho0hXE_q5U";
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            reader.close();
            return jsonResponse.toString();
        } else {
            throw new Exception("Không thể kết nối với API. Mã lỗi: " + responseCode);
        }
    }

    private Map<String, String> parseBookJSON(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray items = jsonObject.optJSONArray("items");
        if (items != null && items.length() > 0) {
            JSONObject firstBook = items.getJSONObject(0).getJSONObject("volumeInfo");
            String title = firstBook.optString("title", "Không có tên sách");
            String language = firstBook.optString("language", "Không có ngôn ngữ");
            String type = firstBook.optJSONArray("categories") != null ?
                    firstBook.optJSONArray("categories").getString(0) : "Không có loại sách";

            Map<String, String> bookDetails = new HashMap<>();
            bookDetails.put("title", title);
            bookDetails.put("language", language);
            bookDetails.put("type", type);

            return bookDetails;
        } else {
            throw new Exception("Không tìm thấy sách với từ khóa đã nhập.");
        }
    }

    private boolean validateBook() {
        boolean isValid = true;

        if (RegExPatterns.namePattern(txtName.getText())) {
            lblNameAlert.setText("Tên không hợp lệ!");
            isValid = false;
        }

        if (RegExPatterns.namePattern(txtLanguage.getText())) {
            lblLanguageAlert.setText("Ngôn ngữ không hợp lệ!");
            isValid = false;
        }

        if (RegExPatterns.namePattern(txtType.getText())) {
            lblTypeAlert.setText("Loại sách không hợp lệ!");
            isValid = false;
        }
        return isValid;
    }

    private void showInfoAlert(String message) {
        System.out.println("INFO: " + message); // Có thể thay bằng Alert hoặc Log
    }

    private void showErrorAlert(String message) {
        System.out.println("ERROR: " + message); // Có thể thay bằng Alert
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnAddOnMouseEntered(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnSelected(addPane, lblAdd);
    }

    @FXML
    void btnAddOnMouseExited(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnUnselected(addPane, lblAdd);
    }

    @FXML
    void btnCancelOnMouseEntered(MouseEvent event) {
        StyleUtil.cancelBtnSelected(cancelPane, lblCancel);
    }

    @FXML
    void btnCancelOnMouseExited(MouseEvent event) {
        StyleUtil.cancelBtnUnselected(cancelPane, lblCancel);
    }

    @FXML
    void btnCloseOnMouseEntered(MouseEvent event) {
        StyleUtil.closeIconBtnSelected(closePane, imgClose);
    }

    @FXML
    void btnCloseOnMouseExited(MouseEvent event) {
        StyleUtil.closeIconBtnUnselected(closePane, imgClose);
    }

    @FXML
    void txtLanguageOnAction(ActionEvent event) {
        txtType.requestFocus();
    }

    @FXML
    void txtTypeOnAction(ActionEvent event) {
        btnAddOnAction(event);
    }

    @FXML
    void txtNameOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtName.getText())) {
            lblNameAlert.setText("Tên không hợp lệ!!");
        } else lblNameAlert.setText(" ");
    }

    @FXML
    void txtLanguageOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtLanguage.getText())) {
            lblLanguageAlert.setText("Ngôn ngữ không hợp lệ!!");
        } else lblLanguageAlert.setText(" ");
    }

    @FXML
    void txtTypeOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtType.getText())) {
            lblTypeAlert.setText("Loại sách không hợp lệ!!");
        } else lblTypeAlert.setText(" ");
    }
}
*/
/*package lk.ijse.bookWormLibraryManagementSystem.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lk.ijse.bookWormLibraryManagementSystem.dto.BookDto;
import lk.ijse.bookWormLibraryManagementSystem.service.ServiceFactory;
import lk.ijse.bookWormLibraryManagementSystem.service.custom.BookService;
import lk.ijse.bookWormLibraryManagementSystem.util.Navigation;
import lk.ijse.bookWormLibraryManagementSystem.util.RegExPatterns;
import lk.ijse.bookWormLibraryManagementSystem.util.StyleUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddBookPopUpFormController {

    @FXML
    private Pane addPane;

    @FXML
    private Pane cancelPane;

    @FXML
    private Pane closePane;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblAdd;

    @FXML
    private Label lblCancel;

    @FXML
    private Label lblLanguageAlert;

    @FXML
    private Label lblNameAlert;

    @FXML
    private Label lblTypeAlert;

    @FXML
    private TextField txtLanguage;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtType;

    @FXML
    private ListView<String> lstBooks;  // ListView để hiển thị danh sách sách tìm được

    private ObservableList<String> booksList = FXCollections.observableArrayList();
    private Map<String, Map<String, String>> booksDetails = new HashMap<>(); // Lưu trữ thông tin chi tiết của sách

    BookService bookService =
            (BookService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceTypes.BOOK);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateBook()) {
            BookDto bookDto = new BookDto();
            bookDto.setName(txtName.getText());
            bookDto.setLanguage(txtLanguage.getText());
            bookDto.setType(txtType.getText());
            bookDto.setAdmin(AdminSignInFormController.admin);
            bookDto.setStatus("Available");

            if (bookService.saveBook(bookDto)) {
                Navigation.closePopUpPane();
                AdminBookManagementFormController.getInstance().allBookId();
            } else {
                System.out.println("Unable to save book!");
            }
        }
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        String keyword = txtName.getText().trim();
        if (!keyword.isEmpty()) {
            try {
                // Gửi yêu cầu đến Google Books API
                String jsonResponse = fetchBookDataFromGoogleBooks(keyword);

                // Phân tích dữ liệu JSON và điền thông tin vào các trường
                Map<String, Map<String, String>> bookDetails = parseBookJSON(jsonResponse);

                // Cập nhật ListView với tất cả các sách tìm được
                updateBooksList(bookDetails);

            } catch (Exception e) {
                showErrorAlert("Lỗi khi tìm kiếm sách: " + e.getMessage());
            }
        } else {
            showErrorAlert("Tên sách không được để trống!");
        }
    }

    private void updateBooksList(Map<String, Map<String, String>> bookDetails) {
        // Cập nhật ListView với danh sách tất cả sách tìm được
        booksList.clear();  // Xóa danh sách cũ
        for (String title : bookDetails.keySet()) {
            booksList.add(title);  // Thêm tên sách vào danh sách
        }
        lstBooks.setItems(booksList);  // Hiển thị danh sách vào ListView
    }

    @FXML
    void onBookSelected(MouseEvent event) {
        String selectedBookTitle = lstBooks.getSelectionModel().getSelectedItem();
        if (selectedBookTitle != null) {
            Map<String, String> selectedBookDetails = booksDetails.get(selectedBookTitle);

            // Điền thông tin chi tiết vào form Add Book
            txtName.setText(selectedBookTitle);
            txtLanguage.setText(selectedBookDetails.get("language"));
            txtType.setText(selectedBookDetails.get("type"));

            // Ẩn Book View
            // Bạn có thể thêm một phương thức để ẩn hoặc đóng cửa sổ Book View ở đây, ví dụ:
            Navigation.closePopUpPane();
        }
    }

    private String fetchBookDataFromGoogleBooks(String keyword) throws Exception {
        String query = keyword.replace(" ", "+");
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=AIzaSyA0hf18hdyylYo_lTEG1jqY1ho0hXE_q5U";
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            reader.close();
            return jsonResponse.toString();
        } else {
            throw new Exception("Không thể kết nối với API. Mã lỗi: " + responseCode);
        }
    }

    private Map<String, Map<String, String>> parseBookJSON(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray items = jsonObject.optJSONArray("items");
        Map<String, Map<String, String>> bookDetails = new HashMap<>();

        if (items != null && items.length() > 0) {
            for (int i = 0; i < items.length(); i++) {
                JSONObject book = items.getJSONObject(i).getJSONObject("volumeInfo");

                String title = book.optString("title", "Không có tên sách");
                String language = book.optString("language", "Không có ngôn ngữ");
                String type = book.optJSONArray("categories") != null ?
                        book.optJSONArray("categories").getString(0) : "Không có loại sách";
                String authors = book.optJSONArray("authors") != null ?
                        book.optJSONArray("authors").join(", ") : "Không có tác giả";
                String isbn = book.optJSONArray("industryIdentifiers") != null ?
                        book.optJSONArray("industryIdentifiers").getJSONObject(0).optString("identifier") : "Không có ISBN";

                // Tạo một Map lưu trữ thông tin chi tiết của từng cuốn sách
                Map<String, String> details = new HashMap<>();
                details.put("language", language);
                details.put("type", type);
                details.put("authors", authors);
                details.put("isbn", isbn);

                bookDetails.put(title, details);
            }
        } else {
            throw new Exception("Không tìm thấy sách với từ khóa đã nhập.");
        }

        return bookDetails;
    }

    private boolean validateBook() {
        boolean isValid = true;

        if (RegExPatterns.namePattern(txtName.getText())) {
            lblNameAlert.setText("Tên không hợp lệ!");
            isValid = false;
        }

        if (RegExPatterns.namePattern(txtLanguage.getText())) {
            lblLanguageAlert.setText("Ngôn ngữ không hợp lệ!");
            isValid = false;
        }

        if (RegExPatterns.namePattern(txtType.getText())) {
            lblTypeAlert.setText("Loại sách không hợp lệ!");
            isValid = false;
        }
        return isValid;
    }

    private void showInfoAlert(String message) {
        System.out.println("INFO: " + message); // Có thể thay bằng Alert thực tế nếu muốn.
    }

    private void showErrorAlert(String message) {
        System.out.println("ERROR: " + message); // Có thể thay bằng Alert thực tế nếu muốn.
    }
    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnAddOnMouseEntered(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnSelected(addPane, lblAdd);
    }

    @FXML
    void btnAddOnMouseExited(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnUnselected(addPane, lblAdd);
    }

    @FXML
    void btnCancelOnMouseEntered(MouseEvent event) {
        StyleUtil.cancelBtnSelected(cancelPane, lblCancel);
    }

    @FXML
    void btnCancelOnMouseExited(MouseEvent event) {
        StyleUtil.cancelBtnUnselected(cancelPane, lblCancel);
    }

    @FXML
    void btnCloseOnMouseEntered(MouseEvent event) {
        StyleUtil.closeIconBtnSelected(closePane, imgClose);
    }

    @FXML
    void btnCloseOnMouseExited(MouseEvent event) {
        StyleUtil.closeIconBtnUnselected(closePane, imgClose);
    }

    @FXML
    void txtLanguageOnAction(ActionEvent event) {
        txtType.requestFocus();
    }

    @FXML
    void txtTypeOnAction(ActionEvent event) {
        btnAddOnAction(event);
    }

    @FXML
    void txtNameOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtName.getText())) {
            lblNameAlert.setText("Tên không hợp lệ!!");
        } else lblNameAlert.setText(" ");
    }

    @FXML
    void txtLanguageOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtLanguage.getText())) {
            lblLanguageAlert.setText("Ngôn ngữ không hợp lệ!!");
        } else lblLanguageAlert.setText(" ");
    }

    @FXML
    void txtTypeOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtType.getText())) {
            lblTypeAlert.setText("Loại sách không hợp lệ!!");
        } else lblTypeAlert.setText(" ");
    }

}
*/
/*package lk.ijse.bookWormLibraryManagementSystem.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lk.ijse.bookWormLibraryManagementSystem.dto.BookDto;
import lk.ijse.bookWormLibraryManagementSystem.service.ServiceFactory;
import lk.ijse.bookWormLibraryManagementSystem.service.custom.BookService;
import lk.ijse.bookWormLibraryManagementSystem.util.Navigation;
import lk.ijse.bookWormLibraryManagementSystem.util.RegExPatterns;
import lk.ijse.bookWormLibraryManagementSystem.util.StyleUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddBookPopUpFormController {

    @FXML
    private Pane addPane;

    @FXML
    private Pane cancelPane;

    @FXML
    private Pane closePane;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblAdd;

    @FXML
    private Label lblCancel;

    @FXML
    private Label lblLanguageAlert;

    @FXML
    private Label lblNameAlert;

    @FXML
    private Label lblTypeAlert;

    @FXML
    private TextField txtLanguage;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtType;

    @FXML
    private ListView<String> lstBooks;

    private ObservableList<String> booksList = FXCollections.observableArrayList();
    private Map<String, Map<String, String>> booksDetails = new HashMap<>();

    private final BookService bookService = (BookService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.BOOK);

    @FXML
    public void initialize() {
        lstBooks.setItems(booksList);
        lstBooks.setOnMouseClicked(this::onBookSelected);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateBook()) {
            BookDto bookDto = new BookDto();
            bookDto.setName(txtName.getText());
            bookDto.setLanguage(txtLanguage.getText());
            bookDto.setType(txtType.getText());
            bookDto.setAdmin(AdminSignInFormController.admin);
            bookDto.setStatus("Available");

            if (bookService.saveBook(bookDto)) {
                Navigation.closePopUpPane();
                AdminBookManagementFormController.getInstance().allBookId();
            } else {
                showErrorAlert("Không thể lưu sách. Vui lòng thử lại!");
            }
        }
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        String keyword = txtName.getText().trim();
        if (!keyword.isEmpty()) {
            try {
                String jsonResponse = fetchBookDataFromGoogleBooks(keyword);
                Map<String, Map<String, String>> bookDetails = parseBookJSON(jsonResponse);
                updateBooksList(bookDetails);
            } catch (Exception e) {
                showErrorAlert("Lỗi khi tìm kiếm sách: " + e.getMessage());
            }
        } else {
            showErrorAlert("Tên sách không được để trống!");
        }
    }

    private void updateBooksList(Map<String, Map<String, String>> bookDetails) {
        booksDetails = bookDetails;
        booksList.clear();
        booksList.addAll(bookDetails.keySet());
        lstBooks.setItems(booksList);
    }

    @FXML
    void onBookSelected(MouseEvent event) {
        String selectedBookTitle = lstBooks.getSelectionModel().getSelectedItem();
        if (selectedBookTitle != null) {
            Map<String, String> selectedBookDetails = booksDetails.get(selectedBookTitle);

            txtName.setText(selectedBookTitle);
            txtLanguage.setText(selectedBookDetails.get("language"));
            txtType.setText(selectedBookDetails.get("type"));
        }
    }

    private String fetchBookDataFromGoogleBooks(String keyword) throws Exception {
        String query = keyword.replace(" ", "+");
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=AIzaSyA0hf18hdyylYo_lTEG1jqY1ho0hXE_q5U";
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            reader.close();
            return jsonResponse.toString();
        } else {
            throw new Exception("Không thể kết nối đến API. Mã lỗi: " + responseCode);
        }
    }

    private Map<String, Map<String, String>> parseBookJSON(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray items = jsonObject.optJSONArray("items");
        Map<String, Map<String, String>> bookDetails = new HashMap<>();

        if (items != null && items.length() > 0) {
            for (int i = 0; i < items.length(); i++) {
                JSONObject book = items.getJSONObject(i).optJSONObject("volumeInfo");

                String title = book != null ? book.optString("title", "Không có tên sách") : "Không có tên sách";
                String language = book != null ? book.optString("language", "Không có ngôn ngữ") : "Không có ngôn ngữ";
                String type = book != null && book.optJSONArray("categories") != null ?
                        book.optJSONArray("categories").optString(0, "Không có loại sách") : "Không có loại sách";

                Map<String, String> details = new HashMap<>();
                details.put("language", language);
                details.put("type", type);

                bookDetails.put(title, details);
            }
        } else {
            throw new Exception("Không tìm thấy sách nào với từ khóa đã nhập.");
        }

        return bookDetails;
    }

    private boolean validateBook() {
        boolean isValid = true;

        if (txtName.getText().isEmpty()) {
            lblNameAlert.setText("Tên sách không được để trống!");
            isValid = false;
        } else {
            lblNameAlert.setText("");
        }

        if (txtLanguage.getText().isEmpty()) {
            lblLanguageAlert.setText("Ngôn ngữ không được để trống!");
            isValid = false;
        } else {
            lblLanguageAlert.setText("");
        }

        if (txtType.getText().isEmpty()) {
            lblTypeAlert.setText("Loại sách không được để trống!");
            isValid = false;
        } else {
            lblTypeAlert.setText("");
        }

        return isValid;
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    @FXML
    void btnAddOnMouseEntered(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnSelected(addPane, lblAdd);
    }

    @FXML
    void btnAddOnMouseExited(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnUnselected(addPane, lblAdd);
    }

    @FXML
    void btnCancelOnMouseEntered(MouseEvent event) {
        StyleUtil.cancelBtnSelected(cancelPane, lblCancel);
    }

    @FXML
    void btnCancelOnMouseExited(MouseEvent event) {
        StyleUtil.cancelBtnUnselected(cancelPane, lblCancel);
    }

    @FXML
    void btnCloseOnMouseEntered(MouseEvent event) {
        StyleUtil.closeIconBtnSelected(closePane, imgClose);
    }

    @FXML
    void btnCloseOnMouseExited(MouseEvent event) {
        StyleUtil.closeIconBtnUnselected(closePane, imgClose);
    }

    @FXML
    void txtLanguageOnAction(ActionEvent event) {
        txtType.requestFocus();
    }

    @FXML
    void txtTypeOnAction(ActionEvent event) {
        btnAddOnAction(event);
    }

    @FXML
    void txtNameOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtName.getText())) {
            lblNameAlert.setText("Tên không hợp lệ!!");
        } else lblNameAlert.setText(" ");
    }

    @FXML
    void txtLanguageOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtLanguage.getText())) {
            lblLanguageAlert.setText("Ngôn ngữ không hợp lệ!!");
        } else lblLanguageAlert.setText(" ");
    }

    @FXML
    void txtTypeOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtType.getText())) {
            lblTypeAlert.setText("Loại sách không hợp lệ!!");
        } else lblTypeAlert.setText(" ");
    }
}

 */
/*package lk.ijse.bookWormLibraryManagementSystem.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lk.ijse.bookWormLibraryManagementSystem.dto.BookDto;
import lk.ijse.bookWormLibraryManagementSystem.service.ServiceFactory;
import lk.ijse.bookWormLibraryManagementSystem.service.custom.BookService;
import lk.ijse.bookWormLibraryManagementSystem.util.Navigation;
import lk.ijse.bookWormLibraryManagementSystem.util.RegExPatterns;
import lk.ijse.bookWormLibraryManagementSystem.util.StyleUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddBookPopUpFormController {

    @FXML
    private Pane addPane;

    @FXML
    private Pane cancelPane;

    @FXML
    private Pane closePane;

    @FXML
    private ImageView imgClose;

    @FXML
    private Label lblAdd;

    @FXML
    private Label lblCancel;

    @FXML
    private Label lblLanguageAlert;

    @FXML
    private Label lblNameAlert;

    @FXML
    private Label lblTypeAlert;

    @FXML
    private TextField txtLanguage;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtType;

    @FXML
    private ListView<String> lstBooks; // Hiển thị danh sách sách với thông tin chi tiết

    private ObservableList<String> booksList = FXCollections.observableArrayList();
    private Map<String, Map<String, String>> booksDetails = new HashMap<>(); // Lưu thông tin chi tiết của sách

    private final BookService bookService = (BookService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.BOOK);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateBook()) {
            BookDto bookDto = new BookDto();
            bookDto.setName(txtName.getText());
            bookDto.setLanguage(txtLanguage.getText());
            bookDto.setType(txtType.getText());
            bookDto.setAdmin(AdminSignInFormController.admin);
            bookDto.setStatus("Available");

            if (bookService.saveBook(bookDto)) {
                Navigation.closePopUpPane();
                AdminBookManagementFormController.getInstance().allBookId();
            } else {
                System.out.println("Unable to save book!");
            }
        }
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        String keyword = txtName.getText().trim();
        if (!keyword.isEmpty()) {
            try {
                // Gửi yêu cầu đến Google Books API
                String jsonResponse = fetchBookDataFromGoogleBooks(keyword);

                // Phân tích dữ liệu JSON và điền thông tin vào danh sách
                booksDetails = parseBookJSON(jsonResponse);

                // Cập nhật danh sách hiển thị
                updateBooksList();

            } catch (Exception e) {
                showErrorAlert("Lỗi khi tìm kiếm sách: " + e.getMessage());
            }
        } else {
            showErrorAlert("Tên sách không được để trống!");
        }
    }

    private void updateBooksList() {
        // Hiển thị thông tin chi tiết của từng cuốn sách
        booksList.clear();
        for (Map.Entry<String, Map<String, String>> entry : booksDetails.entrySet()) {
            String title = entry.getKey();
            Map<String, String> details = entry.getValue();

            String authors = details.getOrDefault("authors", "Không rõ tác giả");
            String language = details.getOrDefault("language", "Không rõ ngôn ngữ");
            String type = details.getOrDefault("type", "Không rõ thể loại");
            String isbn = details.getOrDefault("isbn", "Không rõ ISBN");

            // Hiển thị thông tin dạng: Tên sách - Tác giả - Ngôn ngữ - Thể loại
            String displayText = String.format("%s - %s - %s - %s -%s", title, authors, language, type, isbn);
            booksList.add(displayText);
        }
        lstBooks.setItems(booksList);
    }

    @FXML
    void onBookSelected(MouseEvent event) {
        String selectedBookInfo = lstBooks.getSelectionModel().getSelectedItem();
        if (selectedBookInfo != null) {
            // Lấy tên sách từ phần đầu của chuỗi hiển thị
            String selectedBookTitle = selectedBookInfo.split(" - ")[0];

            if (booksDetails.containsKey(selectedBookTitle)) {
                Map<String, String> selectedBookDetails = booksDetails.get(selectedBookTitle);

                // Điền thông tin chi tiết vào các trường trong bảng Add Book
                txtName.setText(selectedBookTitle);
                txtLanguage.setText(selectedBookDetails.getOrDefault("language", "Không rõ"));
                txtType.setText(selectedBookDetails.getOrDefault("type", "Không rõ"));
            }
        }
    }

    private String fetchBookDataFromGoogleBooks(String keyword) throws Exception {
        String query = keyword.replace(" ", "+");
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=AIzaSyA0hf18hdyylYo_lTEG1jqY1ho0hXE_q5U";
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            reader.close();
            return jsonResponse.toString();
        } else {
            throw new Exception("Không thể kết nối với API. Mã lỗi: " + responseCode);
        }
    }

    private Map<String, Map<String, String>> parseBookJSON(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray items = jsonObject.optJSONArray("items");
        Map<String, Map<String, String>> bookDetails = new HashMap<>();

        if (items != null && items.length() > 0) {
            for (int i = 0; i < items.length(); i++) {
                JSONObject volumeInfo = items.getJSONObject(i).optJSONObject("volumeInfo");
                if (volumeInfo != null) {
                    String title = volumeInfo.optString("title", "Không rõ tên");
                    String language = volumeInfo.optString("language", "Không rõ ngôn ngữ");
                    String type = volumeInfo.optJSONArray("categories") != null
                            ? volumeInfo.optJSONArray("categories").optString(0, "Không rõ thể loại")
                            : "Không rõ thể loại";
                    String authors = volumeInfo.optJSONArray("authors") != null
                            ? volumeInfo.optJSONArray("authors").join(", ").replaceAll("\"", "")
                            : "Không rõ tác giả";
// Lấy ISBN từ industryIdentifiers
                    String isbn = "Không rõ ISBN";
                    if (volumeInfo.has("industryIdentifiers")) {
                        JSONArray identifiers = volumeInfo.getJSONArray("industryIdentifiers");
                        for (int j = 0; j < identifiers.length(); j++) {
                            JSONObject identifier = identifiers.getJSONObject(j);
                            if ("ISBN_13".equals(identifier.optString("type"))) {
                                isbn = identifier.optString("identifier", "Không rõ ISBN");
                                break;
                            }
                        }
                    }
                    // Tạo Map lưu thông tin chi tiết của từng cuốn sách
                    Map<String, String> details = new HashMap<>();
                    details.put("language", language);
                    details.put("type", type);
                    details.put("authors", authors);
                    details.put("isbn", isbn);

                    bookDetails.put(title, details);
                }
            }
        } else {
            throw new Exception("Không tìm thấy sách nào với từ khóa đã nhập.");
        }

        return bookDetails;
    }

    private boolean validateBook() {
        boolean isValid = true;

        if (RegExPatterns.namePattern(txtName.getText())) {
            lblNameAlert.setText("Invalid Name!!");
            isValid = false;
        }

        if (RegExPatterns.namePattern(txtLanguage.getText())) {
            lblLanguageAlert.setText("Invalid Language!!");
            isValid = false;
        }

        if (RegExPatterns.namePattern(txtType.getText())) {
            lblTypeAlert.setText("Invalid Book Type!!");
            isValid = false;
        }
        return isValid;
    }

    private void showErrorAlert(String message) {
        System.out.println("ERROR: " + message); // Có thể thay bằng Alert thực tế nếu cần.
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }



    @FXML
    void btnAddOnMouseEntered(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnSelected(addPane, lblAdd);
    }

    @FXML
    void btnAddOnMouseExited(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnUnselected(addPane, lblAdd);
    }

    @FXML
    void btnCancelOnMouseEntered(MouseEvent event) {
        StyleUtil.cancelBtnSelected(cancelPane, lblCancel);
    }

    @FXML
    void btnCancelOnMouseExited(MouseEvent event) {
        StyleUtil.cancelBtnUnselected(cancelPane, lblCancel);
    }

    @FXML
    void btnCloseOnMouseEntered(MouseEvent event) {
        StyleUtil.closeIconBtnSelected(closePane, imgClose);
    }

    @FXML
    void btnCloseOnMouseExited(MouseEvent event) {
        StyleUtil.closeIconBtnUnselected(closePane, imgClose);
    }

    @FXML
    void txtLanguageOnAction(ActionEvent event) {
        txtType.requestFocus();
    }



    @FXML
    void txtTypeOnAction(ActionEvent event) {
        btnAddOnAction(event);
    }

    @FXML
    void txtNameOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtName.getText())) {
            lblNameAlert.setText("Invalid Name!!");
        } else lblNameAlert.setText(" ");
    }

    @FXML
    void txtLanguageOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtLanguage.getText())) {
            lblLanguageAlert.setText("Invalid Language!!");
        } else lblLanguageAlert.setText(" ");
    }

    @FXML
    void txtTypeOnKeyPressed(KeyEvent event) {
        if (RegExPatterns.namePattern(txtType.getText())) {
            lblTypeAlert.setText("Invalid Book Type!!");
        } else lblTypeAlert.setText(" ");
    }
}
*/

/*tessssssssttttttt */
package LibraryManagementSystem.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import LibraryManagementSystem.controller.QRCodeGenerator;
import LibraryManagementSystem.dto.BookDto;
import LibraryManagementSystem.service.ServiceFactory;
import LibraryManagementSystem.service.custom.BookService;
import LibraryManagementSystem.util.Navigation;
import LibraryManagementSystem.util.StyleUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AddBookPopUpFormController {

    @FXML
    private Pane addPane;
    @FXML
    private Pane cancelPane;
    @FXML
    private Pane closePane;
    @FXML
    private ImageView imgClose;
    @FXML
    private Label lblAdd;
    @FXML
    private Label lblCancel;
    @FXML
    private Label lblLanguageAlert;
    @FXML
    private Label lblNameAlert;
    @FXML
    private Label lblTypeAlert;
    @FXML
    private Label lblAuthorAlert;
    @FXML
    private TextField txtLanguage;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtType;
    @FXML
    private ImageView imgBookCover;
    @FXML
    private ImageView imgQRCode;


    @FXML
    private ListView<String> lstBooks; // Hiển thị danh sách sách với thông tin chi tiết

    private ObservableList<String> booksList = FXCollections.observableArrayList();
    private Map<String, Map<String, String>> booksDetails = new HashMap<>(); // Lưu thông tin chi tiết của sách

    private final BookService bookService = (BookService) ServiceFactory.getInstance().getService(ServiceFactory.ServiceTypes.BOOK);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateBook()) {
            BookDto bookDto = new BookDto();
            bookDto.setName(txtName.getText());
            bookDto.setLanguage(txtLanguage.getText());
            bookDto.setType(txtType.getText());
            bookDto.setAuthor(txtAuthor.getText());
            bookDto.setAdmin(AdminSignInFormController.admin);
            bookDto.setStatus("Available");

            if (bookService.saveBook(bookDto)) {
                Navigation.closePopUpPane();
                AdminBookManagementFormController.getInstance().allBookId();
            } else {
                System.out.println("Unable to save book!");
            }
        }
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        String keyword = txtName.getText().trim();
        if (!keyword.isEmpty()) {
            try {
                // Gửi yêu cầu đến Google Books API
                String jsonResponse = fetchBookDataFromGoogleBooks(keyword);

                // Phân tích dữ liệu JSON và điền thông tin vào danh sách
                booksDetails = parseBookJSON(jsonResponse);

                // Cập nhật danh sách hiển thị
                updateBooksList();

            } catch (Exception e) {
                showErrorAlert("Lỗi khi tìm kiếm sách: " + e.getMessage());
            }
        } else {
            showErrorAlert("Tên sách không được để trống!");
        }
    }

    private void updateBooksList() {
        // Hiển thị thông tin chi tiết của từng cuốn sách
        booksList.clear();
        for (Map.Entry<String, Map<String, String>> entry : booksDetails.entrySet()) {
            String title = entry.getKey();
            Map<String, String> details = entry.getValue();

            String authors = details.getOrDefault("authors", "Không rõ tác giả");
            String language = details.getOrDefault("language", "Không rõ ngôn ngữ");
            String type = details.getOrDefault("type", "Không rõ thể loại");
            String isbn = details.getOrDefault("isbn", "Không rõ ISBN");

            // Hiển thị thông tin dạng: Tên sách - Tác giả - Ngôn ngữ - Thể loại
            String displayText = String.format("%s - %s - %s - %s -%s", title, authors, language, type, isbn);
            booksList.add(displayText);
        }
        lstBooks.setItems(booksList);
    }

    @FXML
    void onBookSelected(MouseEvent event) {
        String selectedBookInfo = lstBooks.getSelectionModel().getSelectedItem();
        if (selectedBookInfo != null) {
            // Lấy tên sách từ phần đầu của chuỗi hiển thị
            String selectedBookTitle = selectedBookInfo.split(" - ")[0];

            if (booksDetails.containsKey(selectedBookTitle)) {
                Map<String, String> selectedBookDetails = booksDetails.get(selectedBookTitle);

                // Điền thông tin chi tiết vào các trường trong bảng Add Book
                txtName.setText(selectedBookTitle);
                txtLanguage.setText(selectedBookDetails.getOrDefault("language", "Không rõ"));
                txtType.setText(selectedBookDetails.getOrDefault("type", "Không rõ"));
                txtAuthor.setText(selectedBookDetails.getOrDefault("authors", "Không rõ"));
                String imagePath = selectedBookDetails.get("image"); // Lấy đường dẫn ảnh bìa
                if (!imagePath.equals("Không có ảnh bìa")) {
                    Image image = new Image(imagePath);  // Tạo đối tượng Image từ URL của ảnh
                    imgBookCover.setImage(image);  // Cập nhật ImageView
                } else {
                    imgBookCover.setImage(null);  // Nếu không có ảnh bìa, xóa ảnh trong ImageView
                }
                // Tạo mã QR từ thông tin sách
                String qrText = "Title: " + selectedBookTitle + "\nAuthor: " + selectedBookDetails.get("authors")
                        + "\nLanguage: " + selectedBookDetails.get("language") + "\nType: " + selectedBookDetails.get("type");
                try {
                    // Sử dụng lớp QRCodeGenerator để tạo mã QR
                    Image qrImage = QRCodeGenerator.generateQRCodeImage(qrText, 200, 200);
                    imgQRCode.setImage(qrImage); // Hiển thị mã QR lên ImageView
                } catch (Exception e) {
                    System.out.println("Lỗi khi tạo mã QR: " + e.getMessage());
                }
            }
        }
    }

    private String fetchBookDataFromGoogleBooks(String keyword) throws Exception {
        String query = keyword.replace(" ", "+");
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&key=AIzaSyA0hf18hdyylYo_lTEG1jqY1ho0hXE_q5U";
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            reader.close();
            return jsonResponse.toString();
        } else {
            throw new Exception("Không thể kết nối với API. Mã lỗi: " + responseCode);
        }
    }

    private Map<String, Map<String, String>> parseBookJSON(String json) throws Exception {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray items = jsonObject.optJSONArray("items");
        Map<String, Map<String, String>> bookDetails = new HashMap<>();

        if (items != null && items.length() > 0) {
            for (int i = 0; i < items.length(); i++) {
                JSONObject volumeInfo = items.getJSONObject(i).optJSONObject("volumeInfo");
                if (volumeInfo != null) {
                    String title = volumeInfo.optString("title", "Không rõ tên");
                    String language = volumeInfo.optString("language", "Không rõ ngôn ngữ");
                    String type = volumeInfo.optJSONArray("categories") != null
                            ? volumeInfo.optJSONArray("categories").optString(0, "Không rõ thể loại")
                            : "Không rõ thể loại";
                    String authors = volumeInfo.optJSONArray("authors") != null
                            ? volumeInfo.optJSONArray("authors").join(", ").replaceAll("\"", "")
                            : "Không rõ tác giả";

                    // Lấy đường dẫn ảnh bìa (thumbnail) từ imageLinks
                    String imageUrl = "Không có ảnh bìa";
                    if (volumeInfo.has("imageLinks")) {
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                        imageUrl = imageLinks.optString("thumbnail", "Không có ảnh bìa");
                    }
// Lấy ISBN từ industryIdentifiers
                    String isbn = "Không rõ ISBN";
                    if (volumeInfo.has("industryIdentifiers")) {
                        JSONArray identifiers = volumeInfo.getJSONArray("industryIdentifiers");
                        for (int j = 0; j < identifiers.length(); j++) {
                            JSONObject identifier = identifiers.getJSONObject(j);
                            if ("ISBN_13".equals(identifier.optString("type"))) {
                                isbn = identifier.optString("identifier", "Không rõ ISBN");
                                break;
                            }
                        }
                    }
                    // Tạo Map lưu thông tin chi tiết của từng cuốn sách
                    Map<String, String> details = new HashMap<>();
                    details.put("language", language);
                    details.put("type", type);
                    details.put("authors", authors);
                    details.put("isbn", isbn);
                    details.put("image", imageUrl); // Thêm đường dẫn ảnh bìa vào đây

                    bookDetails.put(title, details);
                }
            }
        } else {
            throw new Exception("Không tìm thấy sách nào với từ khóa đã nhập.");
        }

        return bookDetails;
    }

    private boolean validateBook() {
        boolean isValid = true;

        // Chỉ kiểm tra xem các trường có bị trống hay không
        if (txtName.getText().trim().isEmpty()) {
            lblNameAlert.setText("Tên sách không được để trống!");
            isValid = false;
        }
        if (txtAuthor.getText().trim().isEmpty()) {
            lblAuthorAlert.setText("Tên tác giả không được để trống!");
            isValid = false;
        }

        if (txtLanguage.getText().trim().isEmpty()) {
            lblLanguageAlert.setText("Ngôn ngữ không được để trống!");
            isValid = false;
        }

        if (txtType.getText().trim().isEmpty()) {
            lblTypeAlert.setText("Thể loại không được để trống!");
            isValid = false;
        }

        return isValid;
    }

    private void showErrorAlert(String message) {
        System.out.println("ERROR: " + message); // Có thể thay bằng Alert thực tế nếu cần.
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }



    @FXML
    void btnAddOnMouseEntered(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnSelected(addPane, lblAdd);
    }

    @FXML
    void btnAddOnMouseExited(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnUnselected(addPane, lblAdd);
    }

    @FXML
    void btnCancelOnMouseEntered(MouseEvent event) {
        StyleUtil.cancelBtnSelected(cancelPane, lblCancel);
    }

    @FXML
    void btnCancelOnMouseExited(MouseEvent event) {
        StyleUtil.cancelBtnUnselected(cancelPane, lblCancel);
    }

    @FXML
    void btnCloseOnMouseEntered(MouseEvent event) {
        StyleUtil.closeIconBtnSelected(closePane, imgClose);
    }

    @FXML
    void btnCloseOnMouseExited(MouseEvent event) {
        StyleUtil.closeIconBtnUnselected(closePane, imgClose);
    }

    @FXML
    void txtLanguageOnAction(ActionEvent event) {
        txtType.requestFocus();
    }



    @FXML
    void txtTypeOnAction(ActionEvent event) {
        btnAddOnAction(event);
    }

    @FXML
    void txtNameOnKeyPressed(KeyEvent event) {
        if (txtName.getText().isEmpty()) {
            lblNameAlert.setText("Invalid Name!!");
        } else lblNameAlert.setText(" ");
    }

    @FXML
    void txtLanguageOnKeyPressed(KeyEvent event) {
        if (txtLanguage.getText().isEmpty()) {
            lblLanguageAlert.setText("Invalid Language!!");
        } else lblLanguageAlert.setText(" ");
    }

    @FXML
    void txtTypeOnKeyPressed(KeyEvent event) {
        if (txtType.getText().isEmpty()) {
            lblTypeAlert.setText("Invalid Book Type!!");
        } else lblTypeAlert.setText(" ");
    }

    @FXML
    void txtAuthorOnKeyPressed(KeyEvent event) {
        if (txtAuthor.getText().isEmpty()) {
            lblTypeAlert.setText("Invalid Author!!");
        } else lblTypeAlert.setText(" ");
    }
}








