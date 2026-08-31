package com.example.ProyectoSpringAndresCastellanos.Service;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.ProductoRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.ProductoResponse;
import com.example.ProyectoSpringAndresCastellanos.Exception.BusinessRuleException;
import com.example.ProyectoSpringAndresCastellanos.Mapper.ProductoMapper;
import com.example.ProyectoSpringAndresCastellanos.Model.Producto;
import com.example.ProyectoSpringAndresCastellanos.Repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    public ProductoResponse crear(ProductoRequest request){
        Producto producto = productoMapper.toEntity(request);
        return productoMapper.toResponse(productoRepository.save(producto));
    }

    public List<ProductoResponse> obtenerTodos(){
        return  productoRepository.findAll()
                .stream()
                .map(productoMapper::toResponse)
                .toList();
    }

    public ProductoResponse obtenerPorId(Long id){
        Producto producto = buscarPorId(id);
        return productoMapper.toResponse(producto);
    }

    public ProductoResponse actualizar(Long id, ProductoRequest request){
        Producto producto = buscarPorId(id);
        producto.setNombre(request.nombre());
        producto.setCategoria(request.categoria());
        producto.setStock(request.stock());
        producto.setPrecio(request.precio());
        return  productoMapper.toResponse(productoRepository.save(producto));
    }

    public void eliminar(Long id){
        Producto producto = buscarPorId(id);
        productoRepository.delete(producto);
    }

    private Producto buscarPorId(Long id){
        return productoRepository.findById(id)
                .orElseThrow(()-> new BusinessRuleException("Producto no encontrado con id: "+ id));
    }
}
