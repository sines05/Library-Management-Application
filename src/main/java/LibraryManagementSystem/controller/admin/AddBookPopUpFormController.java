package LibraryManagementSystem.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyEvent;
import LibraryManagementSystem.dto.BookDto;
import LibraryManagementSystem.service.ServiceFactory;
import LibraryManagementSystem.service.custom.BookService;
import LibraryManagementSystem.util.Navigation;
import LibraryManagementSystem.util.RegExPatterns;
import LibraryManagementSystem.util.StyleUtil;

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

    @FXML
    void btnAddOnMouseEntered(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnSelected(addPane, lblAdd);
    }

    @FXML
    void btnAddOnMouseExited(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnUnselected(addPane, lblAdd);
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
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
    void btnCloseOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
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
    void txtNameOnAction(ActionEvent event) {
        txtLanguage.requestFocus();
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


