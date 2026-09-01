package com.example.ProyectoSpringAndresCastellanos.Service;

import com.example.ProyectoSpringAndresCastellanos.Dto.Response.ProductoMasMovidoResponse;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.ReporteResponse;
import com.example.ProyectoSpringAndresCastellanos.Dto.Response.StockPorBodegaResponse;
import com.example.ProyectoSpringAndresCastellanos.Model.Bodega;
import com.example.ProyectoSpringAndresCastellanos.Model.Movimiento;
import com.example.ProyectoSpringAndresCastellanos.Model.MovimientoDetalle;
import com.example.ProyectoSpringAndresCastellanos.Model.Producto;
import com.example.ProyectoSpringAndresCastellanos.Repository.MovimientoDetalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private static final int TOP_PRODUCTOS_MAS_MOVIDOS = 5;

    private final MovimientoDetalleRepository movimientoDetalleRepository;

    /**
     * Calcula, a partir del historial de movimientos:
     * - stock neto por bodega (entradas a esa bodega - salidas de esa bodega)
     * - los productos que más unidades han movido en total (top 5)
     *
     * Nota: Producto.stock es global (no está atado a una bodega), así que
     * "stock por bodega" no es una columna que exista: se reconstruye sumando
     * los MovimientoDetalle de cada bodega. Es más trabajo, pero refleja
     * realmente el historial auditado en vez de un número suelto.
     */
    public ReporteResponse generarResumen() {
        List<MovimientoDetalle> detalles = movimientoDetalleRepository.findAllConDetalleCompleto();

        Map<Long, String> nombresBodega = new LinkedHashMap<>();
        Map<Long, String> nombresProducto = new LinkedHashMap<>();
        Map<Long, Integer> stockNetoPorBodega = new LinkedHashMap<>();
        Map<Long, Integer> totalMovidoPorProducto = new LinkedHashMap<>();

        for (MovimientoDetalle detalle : detalles) {
            Movimiento movimiento = detalle.getMovimiento();
            Producto producto = detalle.getProducto();
            int cantidad = detalle.getCantidad();

            nombresProducto.put(producto.getId(), producto.getNombre());
            totalMovidoPorProducto.merge(producto.getId(), cantidad, Integer::sum);

            Bodega destino = movimiento.getBodegaDestino();
            if (destino != null) {
                nombresBodega.put(destino.getId(), destino.getNombre());
                stockNetoPorBodega.merge(destino.getId(), cantidad, Integer::sum);
            }

            Bodega origen = movimiento.getBodegaOrigen();
            if (origen != null) {
                nombresBodega.put(origen.getId(), origen.getNombre());
                stockNetoPorBodega.merge(origen.getId(), -cantidad, Integer::sum);
            }
        }

        List<StockPorBodegaResponse> stockPorBodega = new ArrayList<>();
        for (var entry : stockNetoPorBodega.entrySet()) {
            stockPorBodega.add(new StockPorBodegaResponse(
                    entry.getKey(),
                    nombresBodega.get(entry.getKey()),
                    entry.getValue()
            ));
        }

        List<ProductoMasMovidoResponse> productosMasMovidos = totalMovidoPorProducto.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(TOP_PRODUCTOS_MAS_MOVIDOS)
                .map(e -> new ProductoMasMovidoResponse(e.getKey(), nombresProducto.get(e.getKey()), e.getValue()))
                .toList();

        return new ReporteResponse(stockPorBodega, productosMasMovidos);
    }
}