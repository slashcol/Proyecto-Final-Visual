/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectofinal.Explorador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;
import javax.imageio.ImageIO;
import static proyectofinal.Block.FXMLDocumentController.guardar;
import static proyectofinal.Paint.FXMLminipaintController.gimagen;

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

    @FXML private TextArea rutaG,rutaGB;
    TreeItem<String> auxiliar;
    TreeItem<String> seleccionado;
    TreeItem<String> folder;
    private GestorArchivos Archivos;
    String ruta; //ruta del elemento del treeview seleccionado
    String nombreC;
    String[] extension = new String[2];
    String rutaArchivo = null;
    
    /*//ManejoImagenes gimagen = new ManejoImagenes ();
    @FXML private void abrir(ActionEvent e) {
        String nombreA = txtNombreBA.getText();
        buscar(tvArbol.getRoot().getChildren(),nombreA,Archivos.getDirectorio());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Alert alertN = new Alert(Alert.AlertType.ERROR);
        if (rutaArchivo == null) {
            alertN.setTitle("Archivo no encontrado");
            alertN.setHeaderText("No se ha encontrado el archivo");
            alertN.showAndWait();
        }
        if (rutaArchivo != null) {
            alert.setTitle("Archivo Encotrado");
            alert.setHeaderText("¿Desea abrirlo?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                //Abrir la aplicacion
            }
            rutaArchivo=null;
        }
    }*/
    
    public String getRaiz(){
        TreeItem<String> a = tvArbol.getRoot();
        return ruta = "\\"+a.getValue();
    }  
    
   
    private String buscar(ObservableList<TreeItem<String>> nodos,String nombre, String rutaArchivo){
        for(int i=0;i<nodos.size();i++){
            if(nodos.get(i).getValue().equals(nombre) && nodos.get(i).getValue().matches(".*\\..*")){
               this.rutaArchivo = rutaArchivo+"\\"+nombre;
            }else{
               
                    buscar(nodos.get(i).getChildren(),nombre, rutaArchivo+"\\"+nodos.get(i).getValue());
                
               

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
        if (result.isPresent()){
            nombreC = result.get();
            String Path = "..\\datos\\" + ruta + "\\"+nombreC;
            if(!Archivos.checkDirectorio(Path)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Look, an Error Dialog");
                alert.setContentText("El archivo ya existe, ingrese otro nombre");

                alert.showAndWait();
                nuevaVentana(e);
            }else{
                setItem(seleccionado,nombreC);
                
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
        if (result.get() == ButtonType.OK){
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
    
    /*
     * método que pone los iconos en el controler
     */
    private void setIconFiles(ObservableList<TreeItem<String>> aux) {
        control.getChildren().clear();
        control.setPrefColumns(3);
        control.setPrefRows(3);
        String[] nextension = new String[2];
        for (int i = 0; i < aux.size(); i++) {
            Button b = new Button();
            b.setId(ruta);
            nextension = aux.get(i).getValue().split("\\.");
            try {
                b.setGraphic(icono("folder.png", 40, 40));
                if (nextension[1].equals("txt")) {
                    b.setGraphic(icono("txt.png", 40, 40));
                }
                if (nextension[1].equals("jpg") || nextension[1].equals("bmp") || nextension[1].equals("jpeg") || nextension[1].equals("jpe") || nextension[1].equals("jfif") || nextension[1].equals("gif") || nextension[1].equals("tif") || nextension[1].equals("tiff") || nextension[1].equals("png")) {
                    b.setGraphic(icono("mult.png", 40, 40));
                }
            } catch (Exception ex) {
            }

            b.setText(aux.get(i).getValue());
            b.setTextAlignment(TextAlignment.CENTER);
            b.setOnAction((e) -> {

            });
            control.setOrientation(Orientation.HORIZONTAL);
            control.getChildren().addAll(b);
        }
    }
    //
    private void setDirectorio(TreeItem<String> folder, String raiz, String subRaiz) {
        String a = raiz + "\\" + subRaiz;
        String subDirectorio[] = Archivos.contenido(a);
        if (subDirectorio != null) {
            for (int i = 0; i < subDirectorio.length; i++) {
                if (setItem(folder,subDirectorio[i]).equals("carpeta")) {
                    String nuevaRaiz = a + "";
                    setDirectorio(getAuxItem(), a, subDirectorio[i]);
                }
            }
        }
    }
    //
    public String setItem(TreeItem<String> folder, String subDirectorio){
        if (subDirectorio.matches(".*\\..*")) {
            extension = subDirectorio.split("\\.");
            if (extension[1].equals("txt")) {
                TreeItem<String> txt = new TreeItem<>(subDirectorio, icono("txt.png", 20, 20));
                folder.getChildren().add(txt);
                //System.out.println("Matches archivo");
                //return "texto";
            }
            if (extension[1].equals("jpg") || extension[1].equals("bmp")|| extension[1].equals("jpeg") || extension[1].equals("jpe") || extension[1].equals("jfif") || extension[1].equals("gif") || extension[1].equals("tif") || extension[1].equals("tiff") || extension[1].equals("png") ) {
                TreeItem<String> txt = new TreeItem<>(subDirectorio, icono("mult.png", 20, 20));
                folder.getChildren().add(txt);
                //System.out.println("Matches archivo");
                //return "texto";
            }
            return "texto";
        } else {
            TreeItem<String> fold = new TreeItem<>(subDirectorio, icono("folder.png", 20, 20));
            folder.getChildren().add(fold);
            auxiliar = fold;
            //System.out.println("Matches carpeta");
            return "carpeta";
        }
    }
    
    public TreeItem<String> getAuxItem(){
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
    
   
    
    private void explorador(){
        //Mandar el nombre de usuario en el 1
        Archivos = new GestorArchivos("1");
        //Mandar nombre de usuario en el "1"
        folder = new TreeItem<>("1", icono("folder.png", 20, 20));
        tvArbol.setRoot(folder);
        //System.out.println("Obtener root del arbol "+tvArbol.getRoot());
        setDirectorio(folder, Archivos.getDirectorio(), "");
        control.setPadding(new Insets(10, 10, 10, 10));
        control.setVgap(5);
        control.setHgap(5);
        tvArbol.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                TreeItem<String> padre = selectedItem.getParent();
                seleccionado = selectedItem;
                ruta = selectedItem.getValue();
                while (padre != null) {
                    ruta = "\\" + padre.getValue() + "\\" + ruta;
                    padre = padre.getParent();

                }
                //setRuta(ruta);         
                setIconFiles(selectedItem.getChildren());

            }

        });
          //buscar(tvArbol.getRoot().getChildren(),"jeje.txt",Archivos.getDirectorio());
        //System.out.println("hola"+rutaArchivo);
    }

    
}