package com.example.ProyectoSpringAndresCastellanos.Service;

import com.example.ProyectoSpringAndresCastellanos.Dto.Request.ProductoRequest;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.ProductoResponse;
import com.example.ProyectoSpringAndresCastellanos.Exception.BusinessRuleException;
import com.example.ProyectoSpringAndresCastellanos.Mapper.ProductoMapper;
import com.example.ProyectoSpringAndresCastellanos.Model.Producto;
import com.example.ProyectoSpringAndresCastellanos.Model.TipoOperacion;
import com.example.ProyectoSpringAndresCastellanos.Repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;
    private final AuditoriaService auditoriaService;


    public ProductoResponse crear(ProductoRequest request){
        Producto producto = productoMapper.toEntity(request);
        Producto guardado = productoRepository.save(producto);
        auditoriaService.registrar(
                guardado,
                TipoOperacion.INSERT,
                null ,
                guardado.getAuditData()
        );
        return productoMapper.toResponse(guardado);
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
        String valorAnterior = producto.getAuditData();
        producto.setNombre(request.nombre());
        producto.setCategoria(request.categoria());
        producto.setStock(request.stock());
        producto.setPrecio(request.precio());

        Producto actualizado = productoRepository.save(producto);

        String valorNuevo = actualizado.getAuditData();
        auditoriaService.registrar(
                actualizado,
                TipoOperacion.UPDATE,
                valorAnterior,
                valorNuevo
        );
        return productoMapper.toResponse(actualizado);
    }

    public void eliminar(Long id){
        Producto producto = buscarPorId(id);
        String valorAnterior = producto.getAuditData();
        productoRepository.delete(producto);
        auditoriaService.registrar(
                producto,
                TipoOperacion.DELETE,
                valorAnterior,
                null
        );
    }

    private Producto buscarPorId(Long id){
        return productoRepository.findById(id)
                .orElseThrow(()-> new BusinessRuleException("Producto no encontrado con id: "+ id));
    }

}
