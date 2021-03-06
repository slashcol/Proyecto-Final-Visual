/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectofinal.Explorador;

import static de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon.FILE_DOCUMENT;
import static de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon.FILE_IMAGE;
import static de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon.FOLDER;
import static de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon.FOLDER_ACCOUNT;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import proyectofinal.ProyectoFinal;

/**
 * FXML Controller class
 *
 * @author Javier
 */
public class FXMLExplorerController implements Initializable {

    @FXML
    private TreeView tvArbol;
    @FXML
    private TilePane control;

    @FXML
    private TextArea rutaG, rutaGB;
    TreeItem<String> auxiliar;
    TreeItem<String> seleccionado;
    TreeItem<String> folder;
    private GestorArchivos Archivos;
    String ruta; //ruta del elemento del treeview seleccionado
    String nombreC;
    String[] extension = new String[2];
    String rutaArchivo = null;

    public String getRaiz() {
        TreeItem<String> a = tvArbol.getRoot();
        return ruta = "\\" + a.getValue();
    }

    private String buscar(ObservableList<TreeItem<String>> nodos, String nombre, String rutaArchivo) {
        for (int i = 0; i < nodos.size(); i++) {
            if (nodos.get(i).getValue().equals(nombre) && nodos.get(i).getValue().matches(".*\\..*")) {
                this.rutaArchivo = rutaArchivo + "\\" + nombre;
            } else {

                buscar(nodos.get(i).getChildren(), nombre, rutaArchivo + "\\" + nodos.get(i).getValue());
            }
        }
        return null;
    }

    @FXML
    private void nuevaVentana(ActionEvent e) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Carpeta");
        dialog.setHeaderText("Crear nueva carpeta");
        dialog.setContentText("Nombre de la carpeta");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            nombreC = result.get();
            String Path = "..\\datos\\" + ruta + "\\" + nombreC;
            if (!Archivos.checkDirectorio(Path)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Look, an Error Dialog");
                alert.setContentText("El archivo ya existe, ingrese otro nombre");

                alert.showAndWait();
                nuevaVentana(e);
            } else {
                setItem(seleccionado, nombreC);

            }
        }
    }

    @FXML
    private void eliminarVentana(ActionEvent e) throws IOException {
        File fDirectorio;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Carpeta");
        alert.setHeaderText("Si desea eliminar la carpeta seleccion Aceptar");
        String PATH = "..\\datos\\" + ruta;
        fDirectorio = new File(PATH);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            fDirectorio.delete();
            seleccionado.getParent().getChildren().remove(seleccionado);
        } else {
           // TreeItem<String> aux = seleccionado.getParent();

        }
    }

    @FXML
    private void get() {
        System.out.println(tvArbol.getSelectionModel());
    }

    public String getRuta() {
        return ruta;
    }

    private void setIconFiles(String aux) {
        String[] nextension = new String[2];
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(150, 100);

        Button b = new Button();
        b.setLayoutX(0);
        b.setLayoutY(100);
        b.setPrefSize(150, 25);
        b.setId(ruta);
        nextension = aux.split("\\.");
        try {
            pane.getChildren().add(setFolderIcon(70, 40, 75,Color.WHITE));
            pane.setStyle("-fx-background-color:  #FFE082");
            pane.getChildren().add(b);
            b.setStyle("-fx-background-color:  #FFD54F");
            if (nextension[1].equals("txt")) {
                pane.getChildren().remove(0);
                pane.getChildren().add(setFileIcon(70, 40, 75,Color.WHITE));
                pane.setStyle("-fx-background-color:  #40C4FF");
                b.setStyle("-fx-background-color:  #00B0FF");

            }
            if (nextension[1].equals("jpg") || nextension[1].equals("bmp") || nextension[1].equals("jpeg") || nextension[1].equals("jpe") || nextension[1].equals("jfif") || nextension[1].equals("gif") || nextension[1].equals("tif") || nextension[1].equals("tiff") || nextension[1].equals("png")) {
                pane.getChildren().remove(0);
                pane.getChildren().add(setImageIcon(70, 40, 75,Color.WHITE));
                pane.setStyle("-fx-background-color:  #69F0AE;");
                b.setStyle("-fx-background-color:  #00E676;");
            }

        } catch (Exception e) {
        }

        b.setText(aux);
        b.setTextAlignment(TextAlignment.CENTER);
        b.setOnAction((e) -> {

        });
        control.setOrientation(Orientation.HORIZONTAL);
        control.getChildren().addAll(pane);

    }
    /*
     * método que pone los iconos en el controler
     */

    private void setIconFiles(ObservableList<TreeItem<String>> aux) {
        for (int i = 0; i < aux.size(); i++) {
            setIconFiles(aux.get(i).getValue());
        }
    }

    private MaterialDesignIconView setImageIcon(int tam, int x, int y,Color a) {
        MaterialDesignIconView folder = new MaterialDesignIconView(FILE_IMAGE);
        folder.setFont(new Font("MaterialDesignIcons", tam));
        folder.setFill(a);
        folder.setLayoutX(x);
        folder.setLayoutY(y);
        folder.setId("image");
        return folder;
    }

    private MaterialDesignIconView setFileIcon(int tam, int x, int y,Color a) {
        MaterialDesignIconView folder = new MaterialDesignIconView(FILE_DOCUMENT);
        folder.setFont(new Font("MaterialDesignIcons", tam));
        folder.setFill(a);
        folder.setLayoutX(x);
        folder.setLayoutY(y);
        folder.setId("file");
        return folder;
    }

    private MaterialDesignIconView setFolderIcon(int tam, int x, int y,Color a) {
        MaterialDesignIconView folder = new MaterialDesignIconView(FOLDER);
        folder.setFont(new Font("MaterialDesignIcons", tam));
        folder.setFill(a);
        folder.setLayoutX(x);
        folder.setLayoutY(y);
        folder.setId("folder");
        return folder;
    }
    private MaterialDesignIconView setFolderAIcon(int tam, int x, int y,Color a) {
        MaterialDesignIconView folder = new MaterialDesignIconView(FOLDER_ACCOUNT);
        folder.setFont(new Font("MaterialDesignIcons", tam));
        folder.setFill(a);
        folder.setLayoutX(x);
        folder.setLayoutY(y);
        folder.setId("folder");
        return folder;
    }

    private void setDirectorio(TreeItem<String> folder, String raiz, String subRaiz) {
        String a = raiz + "\\" + subRaiz;
        String subDirectorio[] = Archivos.contenido(a);
        if (subDirectorio != null) {
            for (int i = 0; i < subDirectorio.length; i++) {
                if (setItem(folder, subDirectorio[i]).equals("carpeta")) {
                    String nuevaRaiz = a + "";
                    setDirectorio(getAuxItem(), a, subDirectorio[i]);
                }
            }
        }
    }

    //

    public String setItem(TreeItem<String> folder, String subDirectorio) {
        if (subDirectorio.matches(".*\\..*")) {
            extension = subDirectorio.split("\\.");
            if (extension[1].equals("txt")) {
                
                TreeItem<String> txt = new TreeItem<>(subDirectorio,setFileIcon(15, 0, 0,Color.web("#40C4FF")) );
                folder.getChildren().add(txt);
                
            }
            if (extension[1].equals("jpg") || extension[1].equals("bmp") || extension[1].equals("jpeg") || extension[1].equals("jpe") || extension[1].equals("jfif") || extension[1].equals("gif") || extension[1].equals("tif") || extension[1].equals("tiff") || extension[1].equals("png")) {
                TreeItem<String> txt = new TreeItem<>(subDirectorio, setImageIcon(15, 0, 0,Color.web("#69F0AE")));
                folder.getChildren().add(txt);
                //System.out.println("Matches archivo");
                //return "texto";
            }
            return "texto";
        } else {
            TreeItem<String> fold = new TreeItem<>(subDirectorio, setFolderIcon(15, 0, 0,Color.web("#FFE082")));
            folder.getChildren().add(fold);
            auxiliar = fold;
            //System.out.println("Matches carpeta");
            return "carpeta";
        }
    }

    public TreeItem<String> getAuxItem() {
        return auxiliar;
    }

    //

    public ImageView icono(String imagen, double width, double height) {
        Image imageFolder = new Image(getClass().getResourceAsStream(imagen));
        ImageView iF = new ImageView(imageFolder);
        iF.setFitWidth(width);
        iF.setFitHeight(height);
        return iF;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        explorador();
    }

    public void checkCarpeta(String user) {
        Archivos = new GestorArchivos(ProyectoFinal.get().getUser());

    }

    private void explorador() {
        //Mandar el nombre de usuario en el 1
        Archivos = new GestorArchivos(ProyectoFinal.get().getUser());
        //Mandar nombre de usuario en el "1"
        folder = new TreeItem<>(ProyectoFinal.get().getUser(), setFolderAIcon(15, 0, 0,Color.web("#FFE082")));
        tvArbol.setRoot(folder);
        //System.out.println("Obtener root del arbol "+tvArbol.getRoot());
        setDirectorio(folder, Archivos.getDirectorio(), "");
        control.setPadding(new Insets(10, 10, 10, 10));
        control.setVgap(10);
        control.setHgap(10);
        
        tvArbol.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                TreeItem<String> padre = selectedItem.getParent();  
               
                if(seleccionado != null){
                     MaterialDesignIconView icon = (MaterialDesignIconView) seleccionado.getGraphic();
                     switch(icon.getId()){
                         case "folder":
                             icon.setFill(Color.web("#FFE082"));
                             break;
                         case "image":
                             icon.setFill(Color.web("#69F0AE"));
                             break;
                         case "file":
                             icon.setFill(Color.web("#40C4FF"));
                             break;
                     }
                         
                }
                     MaterialDesignIconView icon2 = (MaterialDesignIconView) selectedItem.getGraphic();
                     icon2.setFill(Color.WHITE);
                
                seleccionado = selectedItem;
                ruta = selectedItem.getValue();
                while (padre != null) {
                    ruta = "\\" + padre.getValue() + "\\" + ruta;
                    padre = padre.getParent();

                }
                control.getChildren().clear();
 
                //Método que pone los iconos en el control     
                if (selectedItem.getChildren().isEmpty()) {
                    setIconFiles(selectedItem.getValue());
                } else {
                    setIconFiles(selectedItem.getChildren());
                }

            }

        });
      
    }

}
