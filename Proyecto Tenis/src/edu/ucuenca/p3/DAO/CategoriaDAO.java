/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ucuenca.p3.DAO;

import edu.ucuenca.p3.DAO.exceptions.CategoriaNoExistenteExcepcion;
import edu.ucuenca.p3.DAO.exceptions.CategoriaExistenteExcepcion;
import edu.ucuenca.p3.Modulos.Categoria;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author brian
 */
public class CategoriaDAO { 
    
    private Map<String, Categoria> categorias;
    private static CategoriaDAO instancia;
    
    private CategoriaDAO(){
        categorias = new HashMap<>();
        Categoria categoria1 = new Categoria("pre_juvenil1", 10, 13,"Pre Juvenil");
        Categoria categoria2 = new Categoria("juvenil1", 14, 17, "Juvenil");
        Categoria categoria3 = new Categoria("abierta1", 18, 45, "Abierta");
        categorias.put(categoria1.getCodigo(), categoria1);
        categorias.put(categoria2.getCodigo(), categoria2);
        categorias.put(categoria3.getCodigo(), categoria3);
    }
    
    public static CategoriaDAO getInstancia(){
        if(instancia == null){
            return instancia =  new CategoriaDAO();
        }
        return instancia;
    }
    
    public void insertarCategoria(Categoria categoria) throws CategoriaExistenteExcepcion{
        if(categoria == null){
            throw new IllegalArgumentException("Los campos de categoría no pueden ser nulos !");
        }
        if(categorias.get(categoria.getCodigo())!= null){
            throw new CategoriaExistenteExcepcion("La categoría "+categoria.getNombre()+" ya existe !");
        }
        categorias.put(categoria.getCodigo(), categoria);
    }
    
    public void alterCategoria(Categoria categoria, boolean update) throws CategoriaNoExistenteExcepcion{
        Categoria categoriaAlterna = categorias.get(categoria.getCodigo());
        
        if(categoriaAlterna == null){
            throw new CategoriaNoExistenteExcepcion("La categoría deseada no existe en el registro !");
        }
        if(update){
            categorias.put(categoria.getCodigo(), categoria);
        }else{
            categorias.remove(categoria.getCodigo());
        }
    }
    
    public void modificarCategoria(Categoria categoria) throws CategoriaNoExistenteExcepcion{
        alterCategoria(categoria, true);
    }
    
    public void eliminarCategoria(Categoria categoria) throws CategoriaNoExistenteExcepcion{
        alterCategoria(categoria, false);
    }
    
    public Categoria obtenerCategoria(String codigo){
        if(categorias.get(codigo)!= null){
            return categorias.get(codigo);
        }
        return null;
    }
    
    public Categoria obtenerCategoriaNombre(String nombre){
        Categoria categoria = null;
        for (Map.Entry<String, Categoria> entrySet : categorias.entrySet()) {
            Categoria value = entrySet.getValue();
            if(value.getNombre().equalsIgnoreCase(nombre)){
                categoria = categorias.get(value.getCodigo());
            }
        }
        return categoria;
    }
    
    public List<Categoria> listarCategorias(){
        List<Categoria> lista_categoria = new ArrayList<>();
        for (Map.Entry<String, Categoria> entrySet : categorias.entrySet()) {
            Categoria value = entrySet.getValue();
            lista_categoria.add(value);
        }
        return lista_categoria;
    }

}
