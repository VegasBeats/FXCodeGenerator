package dad.codegen.ui.main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import dad.codegen.model.javafx.Bean;
import dad.codegen.model.javafx.FXModel;
import dad.codegen.ui.beans.BeanController;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class MainController implements Initializable {

	@FXML
    private Button abrirButton;

    @FXML
    private ListView<Bean> beansTable;

    @FXML
    private Button eliminarBeanAction;

    @FXML
    private Button generarCodigoButton;

    @FXML
    private VBox noSeleccionadoBox;

    @FXML
    private Button nuevoBeanButton;

    @FXML
    private Button nuevoButton;

    @FXML
    private Button guardarButton;

    @FXML
    private TextField paqueteField;

    @FXML
    private BorderPane rightView;

    @FXML
    private VBox view;
    
    private BeanController beanController = new BeanController();
	
    private MainModel model = new MainModel();
    
    private Alert nuevoAlert;
    
	public MainController() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
			loader.setController(this);
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		model.paqueteProperty().bindBidirectional(paqueteField.textProperty());
		model.beansProperty().bindBidirectional(beansTable.itemsProperty());
		
		model.getFxmodel().packageNameProperty().bindBidirectional(model.paqueteProperty());
		model.getFxmodel().beansProperty().bindBidirectional(model.beansProperty());
		
		model.selectedBeanProperty().bind(beansTable.getSelectionModel().selectedItemProperty());
		
		beanController.getPadreComboBox().itemsProperty().bindBidirectional(model.beansProperty());
		
		nuevoAlert = new Alert(AlertType.CONFIRMATION,"¿Desea Continuar?");
    	nuevoAlert.setHeaderText("Se va a crear un nuevo ModeloFX. \n "
    			+ "\n"
    			+ "Los cambios que haya realizado en el model actual se perderán. "
    	);
		
		beansTable.setCellFactory(param -> new ListCell<Bean>() {
			@Override
		    protected void updateItem(Bean item, boolean empty) {
		        super.updateItem(item, empty);

		        if (empty || item == null || item.getName() == null) {
		            setText(null);
		        } else {
		            setText(item.getName());
		        }
		    }
		});
		
		eliminarBeanAction.disableProperty().bind(model.beansProperty().emptyProperty());
		
		beansTable.getSelectionModel().selectedItemProperty().addListener((obv, ov, nv) -> onBeanChanged(obv,ov,nv));
		
		model.fxmodelProperty().addListener((obv, ov, nv) -> onFXModelChanged(obv,ov,nv));
		
		Bean bean = new Bean();
		bean.setName("Hola");
		beansTable.getItems().add(bean);
	}
	
	private void onFXModelChanged(ObservableValue<? extends FXModel> obv, FXModel ov, FXModel nv) {
		model.setBeans(nv.getBeans());
	}

	public void onBeanChanged(ObservableValue<? extends Bean> obv, Bean ov, Bean nv) {
		if (ov != null) {
			beanController.getModel().nombreProperty().unbindBidirectional(ov.nameProperty());
			beanController.getModel().padreProperty().unbindBidirectional(ov.parentProperty());
			beanController.getModel().propiedadesProperty().unbindBidirectional(ov.propertiesProperty());
		}
		
		if (nv != null) {
			beanController.getModel().nombreProperty().bindBidirectional(nv.nameProperty());
			beanController.getModel().padreProperty().bindBidirectional(nv.parentProperty());
			beanController.getModel().propiedadesProperty().bindBidirectional(nv.propertiesProperty());
		}
	}

	@FXML
    void onAbrirButton(ActionEvent event) {
		nuevoAlert.initOwner(App.primaryStage);
		nuevoAlert.showAndWait()
		.filter(response -> response == ButtonType.OK)
		.ifPresent(response -> {
			clear();
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("FXModel", "*.fx")
				    ,new FileChooser.ExtensionFilter("Todos los Archivos", "*.*")
			);
			fc.setInitialDirectory(new File("."));
			File fxModelFile = fc.showOpenDialog(App.primaryStage);
			try {
				model.setFxmodel(FXModel.load(fxModelFile));
				System.out.println(model.getFxmodel());
				
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.ERROR, e.getMessage());
				alert.setHeaderText("Error al abrir el modelo FX desde el fichero '"+fxModelFile.getName()+"'.");
				alert.initOwner(App.primaryStage);
				alert.showAndWait();
				e.printStackTrace();
			}
		});
    }
	
	@FXML
    void onBeansListClickedAction(MouseEvent event) {
		rightView.setCenter(
				(Objects.isNull(beansTable.getSelectionModel().getSelectedItem()) 
						? noSeleccionadoBox
						: beanController.getView()
				)
		);
    }

    @FXML
    void onEliminarBeanAction(ActionEvent event) {
    	Bean bean = beansTable.getSelectionModel().getSelectedItem();
    	Alert alert = new Alert(AlertType.CONFIRMATION, "¿Desea Continuar?");
    	alert.setHeaderText("Se va a eliminar el bean '"+bean.getName()+"'");
    	alert.showAndWait()
    		.filter(response -> response == ButtonType.OK)
    		.ifPresent(response -> {
    			model.beansProperty().remove(bean);
    		});
    }

    @FXML
    void onGenerarCodigoAction(ActionEvent event) {
    	DirectoryChooser dirChooser = new DirectoryChooser();
    	File directorio = dirChooser.showDialog(App.primaryStage);
    	if (directorio != null) {
    		try {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Se ha generado el código correctamente en el directorio '"+directorio.getName()+"'");
				model.getFxmodel().generateCode(directorio);
				alert.showAndWait();
			} catch (IOException e) {
				Alert errorAlert = new Alert(AlertType.ERROR, e.getMessage());
				errorAlert.setHeaderText("Error al generar el código en el directorio '"+directorio.getName()+"'.");
				errorAlert.showAndWait();
				e.printStackTrace();
			}
    	}
    }

    @FXML
    void onGuardarAction(ActionEvent event) {
    	FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("FXModel", "*.fx")
			    ,new FileChooser.ExtensionFilter("Todos los Archivos", "*.*")
		);
		fc.setInitialDirectory(new File("."));
		File fxModelFile = fc.showSaveDialog(App.primaryStage);
		try {
			model.getFxmodel().save(fxModelFile);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR, e.getMessage());
			alert.setHeaderText("Error al guardar el modelo FX desde el fichero '"+fxModelFile.getName()+"'.");
			alert.initOwner(App.primaryStage);
			alert.showAndWait();
			e.printStackTrace();
		}
    }
    
    @FXML
    void onNuevoAction(ActionEvent event) {
    	nuevoAlert.initOwner(App.primaryStage);
    	nuevoAlert.showAndWait()
    		.filter(response -> response == ButtonType.OK)
    		.ifPresent(response -> clear());
    }
    
    private void clear() {
    	paqueteField.clear();
    	rightView.setCenter(noSeleccionadoBox);
    	beanController.clear();
    	beansTable.getItems().clear();
    }

    @FXML
    void onNuevoBeanAction(ActionEvent event) {
    	Bean nuevoBean = new Bean();
    	nuevoBean.setName("Nuevo Bean");
    	model.beansProperty().add(nuevoBean);
    	beansTable.getSelectionModel().select(nuevoBean);
    }
    
    public Button getAbrirButton() {
		return abrirButton;
	}

	public ListView<?> getBeansTable() {
		return beansTable;
	}

	public Button getEliminarBeanAction() {
		return eliminarBeanAction;
	}

	public Button getGenerarCodigoButton() {
		return generarCodigoButton;
	}

	public VBox getNoSeleccionadoBox() {
		return noSeleccionadoBox;
	}

	public Button getNuevoBeanButton() {
		return nuevoBeanButton;
	}

	public Button getNuevoButton() {
		return nuevoButton;
	}

	public Button getOnGuardarButton() {
		return guardarButton;
	}

	public TextField getPaqueteField() {
		return paqueteField;
	}

	public BorderPane getRightView() {
		return rightView;
	}

	public VBox getView() {
		return view;
	}
}
