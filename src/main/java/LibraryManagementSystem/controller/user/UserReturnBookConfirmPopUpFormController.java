package LibraryManagementSystem.controller.user;

import LibraryManagementSystem.controller.user.UserBorrowedBooksBarFormController;
import LibraryManagementSystem.controller.user.UserBorrowedBooksFormController;
import LibraryManagementSystem.controller.user.UserSignInFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import LibraryManagementSystem.dto.TransactionDto;
import LibraryManagementSystem.entity.Book;
import LibraryManagementSystem.entity.TransactionDetail;
import LibraryManagementSystem.service.ServiceFactory;
import LibraryManagementSystem.service.custom.TransactionService;
import LibraryManagementSystem.util.Navigation;
import LibraryManagementSystem.util.StyleUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserReturnBookConfirmPopUpFormController implements Initializable {

    @FXML
    private Pane cancelPane;

    @FXML
    private Label lblCancel;

    @FXML
    private Label lblDueDate;

    @FXML
    private Label lblId;

    @FXML
    private Label lblReturn;

    @FXML
    private Label lblTotalBooks;

    @FXML
    private Pane returnPane;

    @FXML
    private VBox vBox;

    private TransactionDto transactionDto;

    public static List<Book> booksToBeReturned = new ArrayList<>();

    TransactionService transactionService =
            (TransactionService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceTypes.TRANSACTION);

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        Navigation.closeUserPopUpLargePane();
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
    void btnReturnOnAction(ActionEvent event) {
        TransactionDto updatedTransactionDto = new TransactionDto();
        updatedTransactionDto.setId(Integer.parseInt(lblId.getText()));
        updatedTransactionDto.setTransactionType("return");
        updatedTransactionDto.setBookQty(Integer.parseInt(lblTotalBooks.getText()));
        updatedTransactionDto.setDueDate(lblDueDate.getText());
        updatedTransactionDto.setDateAndTime(transactionDto.getDateAndTime());
        updatedTransactionDto.setUser(UserSignInFormController.user);

        if (transactionService.updateTransaction(updatedTransactionDto)) {
            Navigation.closeUserPopUpLargePane();
            UserBorrowedBooksFormController.getInstance().allBorrowedTransactionId();
            //AdminBorrowedBookFormController.getInstance().allBorrowedTransactionId();
        } else {
            System.out.println("Unable to update transaction!");
        }
    }

    @FXML
    void btnReturnOnMouseEntered(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnSelected(returnPane, lblReturn);
    }

    @FXML
    void btnReturnOnMouseExited(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnUnselected(returnPane, lblReturn);
    }

    public void allBorrowedBookId() {
        setData();
        TransactionDto dto = transactionService
                .getTransactionData(UserBorrowedBooksBarFormController.transactionId);

        List<TransactionDetail> transactionDetails = dto.getTransactionDetails();

        vBox.getChildren().clear();
        if (transactionDetails == null) return;

        for (TransactionDetail transactionDetail : transactionDetails) {
            booksToBeReturned.add(transactionDetail.getBook());
            loadDataTable(transactionDetail.getBook().getId());
        }
    }

    private void loadDataTable(int id) {
        try {
            FXMLLoader loader = new FXMLLoader(UserReturnBookConfirmPopUpFormController.class.getResource("/view/userReturnBookConfirmPopUpBarForm.fxml"));
            Parent root = loader.load();
            UserReturnBookConfirmPopUpBarFormController controller = loader.getController();
            controller.setData(id);
            vBox.getChildren().add(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setData() {
        transactionDto = transactionService
                .getTransactionData(UserBorrowedBooksBarFormController.transactionId);

        lblDueDate.setText(transactionDto.getDueDate());
        lblId.setText(String.valueOf(transactionDto.getId()));
        lblTotalBooks.setText(String.valueOf(transactionDto.getBookQty()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allBorrowedBookId();
    }

}
