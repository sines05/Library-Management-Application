package LibraryManagementSystem.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import LibraryManagementSystem.dto.BookDto;
import LibraryManagementSystem.service.ServiceFactory;
import LibraryManagementSystem.service.custom.BookService;
import LibraryManagementSystem.util.StyleUtil;

public class UserBorrowBookConfirmPopUpBarFormController {

    @FXML
    private ImageView imgDelete;

    @FXML
    private Label lblId;

    @FXML
    private Label lblLanguage;

    @FXML
    private Label lblName;

    @FXML
    private Label lblType;
    @FXML
    private Label lblAuthor;

    BookDto bookDto;

    BookService bookService =
            (BookService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceTypes.BOOK);

    @FXML
    void imgDeleteOnMouseClicked(MouseEvent event) {
        UserBorrowBooksFormController.getInstance().borrowedBooks.remove(bookDto);
        UserBorrowBookConfirmPopUpFormController.getInstance().allBorrowedBookId();
    }

    @FXML
    void imgDeleteOnMouseEntered(MouseEvent event) {
        StyleUtil.deleteImgSelected(imgDelete);
    }

    @FXML
    void imgDeleteOnMouseExited(MouseEvent event) {
        StyleUtil.deleteImgUnselected(imgDelete);
    }

    public void setData(int id) {
        if (bookDto == null) {
            // Hiển thị thông báo hoặc xử lý khi không tìm thấy cuốn sách
            lblId.setText("Không tìm thấy sách");
            lblName.setText("Không có tên sách");
            lblType.setText("Không có loại sách");
            lblLanguage.setText("Không có ngôn ngữ");
            lblAuthor.setText("Không có tác giả"); }
        bookDto = bookService.getBookData(id);

        lblId.setText(String.valueOf(bookDto.getId()));
        lblName.setText(bookDto.getName());
        lblType.setText(bookDto.getType());
        lblLanguage.setText(bookDto.getLanguage());
        lblAuthor.setText(bookDto.getAuthor());
    }

}
