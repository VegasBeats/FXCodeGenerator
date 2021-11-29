package dad.codegen.ui.beans;

import dad.codegen.model.javafx.Bean;
import dad.codegen.model.javafx.Property;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class BeanModel {

	private StringProperty nombre;

	private ObjectProperty<Bean> padre;

	private ListProperty<Property> propiedades;
	
	private ObjectProperty<Property> selectedProperty;
	
	public BeanModel() {
		nombre = new SimpleStringProperty();
		padre = new SimpleObjectProperty<Bean>(new Bean());
		propiedades = new SimpleListProperty<Property>();
		selectedProperty = new SimpleObjectProperty<Property>();
	}

	public final StringProperty nombreProperty() {
		return this.nombre;
	}

	public final String getNombre() {
		return this.nombreProperty().get();
	}

	public final void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	public final ObjectProperty<Bean> padreProperty() {
		return this.padre;
	}

	public final Bean getPadre() {
		return this.padreProperty().get();
	}

	public final void setPadre(final Bean padre) {
		this.padreProperty().set(padre);
	}

	public final ListProperty<Property> propiedadesProperty() {
		return this.propiedades;
	}

	public final ObservableList<Property> getPropiedades() {
		return this.propiedadesProperty().get();
	}

	public final void setPropiedades(final ObservableList<Property> propiedades) {
		this.propiedadesProperty().set(propiedades);
	}

	public final ObjectProperty<Property> selectedPropertyProperty() {
		return this.selectedProperty;
	}

	public final Property getSelectedProperty() {
		return this.selectedPropertyProperty().get();
	}

	public final void setSelectedProperty(final Property selectedProperty) {
		this.selectedPropertyProperty().set(selectedProperty);
	}
}
