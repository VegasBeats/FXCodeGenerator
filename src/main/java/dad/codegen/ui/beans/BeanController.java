package dad.codegen.ui.beans;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import dad.codegen.model.javafx.Bean;
import dad.codegen.model.javafx.Property;
import dad.codegen.model.javafx.Type;
import dad.codegen.ui.main.App;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class BeanController implements Initializable {

	@FXML
    private Button editarPropertyButton;

    @FXML
    private Button eliminarPadreButton;

    @FXML
    private Button eliminarPropertyButton;

    @FXML
    private TableColumn<Property, Bean> genericoColumn;

    @FXML
    private TableColumn<Property, String> nombreColumn;

    @FXML
    private TextField nombreField;

    @FXML
    private Button nuevaPropertyButton;

    @FXML
    private ComboBox<Bean> padreComboBox;

    @FXML
    private TableView<Property> propertiesTableView;

    @FXML
    private TableColumn<Property, Boolean> soloLecturaColumn;

    @FXML
    private TableColumn<Property, Type> tipoColumn;

    @FXML
    private GridPane view;
	
    private BeanModel model = new BeanModel();
    
	public BeanController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/BeansView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		nombreColumn.setCellValueFactory(new PropertyValueFactory<Property, String>("name"));
		soloLecturaColumn.setCellValueFactory(
				new Callback<CellDataFeatures<Property,Boolean>,ObservableValue<Boolean>>()
				{
				    @Override
				    public ObservableValue<Boolean> call(CellDataFeatures<Property, Boolean> param)
				    {   
				        return param.getValue().readOnlyProperty();
				    }
				}
		);
		
		soloLecturaColumn.setCellFactory(
				CheckBoxTableCell.forTableColumn(soloLecturaColumn)
		);
		
		tipoColumn.setCellValueFactory(new PropertyValueFactory<Property, Type>("type"));
		genericoColumn.setCellValueFactory(new PropertyValueFactory<Property, Bean>("generic"));
		
		nombreField.textProperty().bindBidirectional(model.nombreProperty());
		padreComboBox.valueProperty().bindBidirectional(model.padreProperty());
		propertiesTableView.itemsProperty().bindBidirectional(model.propiedadesProperty());
		model.selectedPropertyProperty().bind(propertiesTableView.getSelectionModel().selectedItemProperty());
		
		editarPropertyButton.disableProperty().bind(model.propiedadesProperty().emptyProperty());
		
		eliminarPropertyButton.disableProperty().bind(model.propiedadesProperty().emptyProperty());
	}
	
	@FXML
    void onEditarPropertyAction(ActionEvent event) {
		
    }

    @FXML
    void onEliminarPadreAction(ActionEvent event) {
    	model.padreProperty().set(null);
    }

    @FXML
    void onEliminarPropertyAction(ActionEvent event) {
    	if (!Objects.isNull(model.getSelectedProperty())) {
    		Property selected = model.getSelectedProperty();
        	Alert eliminarAlert = new Alert(AlertType.CONFIRMATION, "¿Estás seguro?");
        	eliminarAlert.setHeaderText("Se va a eliminar la propiedad '"+selected.getName()+"'");
        	eliminarAlert.initOwner(App.primaryStage);
        	eliminarAlert.showAndWait()
        		.filter(response -> response == ButtonType.OK)
        		.ifPresent(response -> {
        			model.propiedadesProperty().remove(selected);
        		});
    	}
    }

    @FXML
    void onNuevaPropertyAction(ActionEvent event) {
    	Property property = new Property();
    	property.setName("nuevaPropiedad");
    	property.setType(Type.STRING);
    	property.setReadOnly(false);
    	property.setGeneric(null);
    	
    	model.propiedadesProperty().add(property);
    }
    
    public void clear() {
    	nombreField.clear();
    	padreComboBox.getEditor().clear();
    	if (!Objects.isNull(propertiesTableView.getItems())) {
    		propertiesTableView.getItems().clear();
    	}
	}
    
	public BeanModel getModel() {
		return model;
	}

	public Button getEditarPropertyButton() {
		return editarPropertyButton;
	}

	public Button getEliminarPadreButton() {
		return eliminarPadreButton;
	}

	public Button getEliminarPropertyButton() {
		return eliminarPropertyButton;
	}

	public TableColumn<?, ?> getGenericoColumn() {
		return genericoColumn;
	}

	public TableColumn<?, ?> getNombreColumn() {
		return nombreColumn;
	}

	public TextField getNombreField() {
		return nombreField;
	}

	public Button getNuevaPropertyButton() {
		return nuevaPropertyButton;
	}

	public ComboBox<Bean> getPadreComboBox() {
		return padreComboBox;
	}

	public TableView<?> getPropertiesTableView() {
		return propertiesTableView;
	}

	public TableColumn<?, ?> getSoloLecturaColumn() {
		return soloLecturaColumn;
	}

	public TableColumn<?, ?> getTipoColumn() {
		return tipoColumn;
	}

	public GridPane getView() {
		return view;
	}
}
