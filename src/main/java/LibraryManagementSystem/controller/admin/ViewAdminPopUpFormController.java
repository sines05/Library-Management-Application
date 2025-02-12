package LibraryManagementSystem.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import LibraryManagementSystem.dto.AdminDto;
import LibraryManagementSystem.service.ServiceFactory;
import LibraryManagementSystem.service.custom.AdminService;
import LibraryManagementSystem.util.Navigation;
import LibraryManagementSystem.util.StyleUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewAdminPopUpFormController implements Initializable {

    @FXML
    private Pane closePane;

    @FXML
    private Pane exitPane;

    @FXML
    private ImageView imgExit;

    @FXML
    private Label lblClose;

    @FXML
    private Label lblContactNo;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblId;

    @FXML
    private Label lblName;

    @FXML
    private Label lblUsername;

    public static int adminId;

    AdminService adminService =
            (AdminService) ServiceFactory.getInstance()
                    .getService(ServiceFactory.ServiceTypes.ADMIN);

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnCloseOnMouseEntered(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnSelected(closePane, lblClose);
    }

    @FXML
    void btnCloseOnMouseExited(MouseEvent event) {
        StyleUtil.addUpdateConfirmReturnBtnUnselected(closePane, lblClose);
    }

    @FXML
    void btnExitOnAction(ActionEvent event) {
        Navigation.closePopUpPane();
    }

    @FXML
    void btnExitOnMouseEntered(MouseEvent event) {
        StyleUtil.closeIconBtnSelected(exitPane, imgExit);
    }

    @FXML
    void btnExitOnMouseExited(MouseEvent event) {
        StyleUtil.closeIconBtnUnselected(exitPane, imgExit);
    }

    public void setData() {
        AdminDto dto = adminService.getAdminData(adminId);

        lblId.setText(String.valueOf(dto.getId()));
        lblContactNo.setText(dto.getContactNo());
        lblEmail.setText(dto.getEmail());
        lblUsername.setText(dto.getUsername());
        lblName.setText(
                dto.getName().getFirstName() + " " +
                        dto.getName().getLastName()
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setData();
    }

}
