package dad.codegen.ui.main;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import dad.codegen.model.javafx.Bean;
import dad.codegen.model.javafx.FXModel;

public class MainModel {

	private StringProperty paquete;
	
	private ObjectProperty<FXModel> fxmodel;
	
	private ListProperty<Bean> beans;
	
	private ObjectProperty<Bean> selectedBean;
	
	public MainModel() {
		paquete = new SimpleStringProperty();
		fxmodel = new SimpleObjectProperty<>(new FXModel());
		beans = new SimpleListProperty<>();
		selectedBean = new SimpleObjectProperty<Bean>(new Bean());
	}

	public final StringProperty paqueteProperty() {
		return this.paquete;
	}

	public final String getPaquete() {
		return this.paqueteProperty().get();
	}

	public final void setPaquete(final String paquete) {
		this.paqueteProperty().set(paquete);
	}

	public final ObjectProperty<FXModel> fxmodelProperty() {
		return this.fxmodel;
	}

	public final FXModel getFxmodel() {
		return this.fxmodelProperty().get();
	}

	public final void setFxmodel(final FXModel fxmodel) {
		this.fxmodelProperty().set(fxmodel);
	}

	public final ListProperty<Bean> beansProperty() {
		return this.beans;
	}

	public final ObservableList<Bean> getBeans() {
		return this.beansProperty().get();
	}

	public final void setBeans(final ObservableList<Bean> beans) {
		this.beansProperty().set(beans);
	}

	public final ObjectProperty<Bean> selectedBeanProperty() {
		return this.selectedBean;
	}
	
	public final Bean getSelectedBean() {
		return this.selectedBeanProperty().get();
	}

	public final void setSelectedBean(final Bean selectedBean) {
		this.selectedBeanProperty().set(selectedBean);
	}
}
