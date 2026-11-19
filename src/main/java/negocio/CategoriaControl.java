package negocio;

import datos.CategoriaDAO;
import entidades.Categoria;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author JcarlosAd7
 */
public class CategoriaControl {
    private final CategoriaDAO DATOS;
    private Categoria obj;
    private DefaultTableModel modeloTabla;
    // MEJORA 1: antes era public. Ya existe totalMostrados() para leerlo,
    // así que nadie de afuera debería poder modificarlo directamente.
    private int registrosMostrados;
    
    public CategoriaControl(){
        this.DATOS=new CategoriaDAO();
        this.obj=new Categoria();
        this.registrosMostrados=0;
    }
    
    public DefaultTableModel listar(String texto){
        // MEJORA 2: antes era "new ArrayList()" sin tipo (raw type), que da warning
        // y pierde la seguridad de tipos. Con <> Java infiere que es de Categoria.
        // Además se crea y llena en una sola línea.
        List<Categoria> lista=new ArrayList<>(DATOS.listar(texto));
        
        String[] titulos={"Id","Nombre","Descripción","Estado"};
        this.modeloTabla=new DefaultTableModel(null,titulos);        
        
        String estado;
        String[] registro = new String[4];
        
        this.registrosMostrados=0;
        for (Categoria item:lista){
            // MEJORA 3: el if/else de 5 líneas se reduce a un operador ternario:
            // condición ? valorSiTrue : valorSiFalse
            estado = item.isActivo() ? "Activo" : "Inactivo";
            registro[0]=Integer.toString(item.getId());
            registro[1]=item.getNombre();
            registro[2]=item.getDescripcion();
            registro[3]=estado;
            this.modeloTabla.addRow(registro);
            // MEJORA 4: ++ hace lo mismo que "x = x + 1", más corto y legible.
            this.registrosMostrados++;
        }
        return this.modeloTabla;
    }
    
    public String insertar(String nombre,String descripcion){
        if (DATOS.existe(nombre)){
            return "El registro ya existe.";
        }else{
            obj.setNombre(nombre);
            obj.setDescripcion(descripcion);
            if (DATOS.insertar(obj)){
                return "OK";
            }else{
                return "Error en el registro.";
            }
        }
    }
    
    public String actualizar(int id, String nombre,String nombreAnt,String descripcion){
        // MEJORA 5: antes el bloque de setId/setNombre/setDescripcion/actualizar
        // estaba DUPLICADO en dos ramas. Ahora solo validamos una cosa:
        // "si el nombre cambió Y el nuevo ya existe -> error".
        // Si no, se actualiza una sola vez. Mismo comportamiento, la mitad de código.
        if (!nombre.equals(nombreAnt) && DATOS.existe(nombre)){
            return "El registro ya existe.";
        }
        obj.setId(id);
        obj.setNombre(nombre);
        obj.setDescripcion(descripcion);
        if (DATOS.actualizar(obj)){
            return "OK";
        }else{
            return "Error en la actualización.";
        }
    }
    
    public String desactivar(int id){
        if (DATOS.desactivar(id)){
            return "OK";
        }else{
            return "No se puede desactivar el registro";
        }
    }
    
    public String activar(int id){
        if (DATOS.activar(id)){
            return "OK";
        }else{
            return "No se puede activar el registro";
        }
    }
    
    public int total(){
        return DATOS.total();
    }
    
    public int totalMostrados(){
        return this.registrosMostrados;
    }
}
