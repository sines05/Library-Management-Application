package LibraryManagementSystem.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import LibraryManagementSystem.dto.BookDto;
import LibraryManagementSystem.service.ServiceFactory;
import LibraryManagementSystem.service.custom.BookService;

public class UserReturnBookConfirmPopUpBarFormController {

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

    BookService bookService =
            (BookService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceTypes.BOOK);

    public void setData(int id) {
        BookDto bookDto = bookService.getBookData(id);

        lblId.setText(String.valueOf(bookDto.getId()));
        lblName.setText(bookDto.getName());
        lblType.setText(bookDto.getType());
        lblLanguage.setText(bookDto.getLanguage());
        lblAuthor.setText(bookDto.getAuthor());
    }

}
