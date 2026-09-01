package com.example.ProyectoSpringAndresCastellanos.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movimientos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movimiento implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "bodega_origen_id")
    private Bodega bodegaOrigen;

    @ManyToOne
    @JoinColumn(name = "bodega_destino_id")
    private Bodega bodegaDestino;

    @OneToMany(
            mappedBy = "movimiento",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MovimientoDetalle> detalles = new ArrayList<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getAuditData() {

        return "id=" + id +
                ", fecha=" + fecha +
                ", tipoMovimiento=" + tipoMovimiento +
                ", usuario=" +
                (usuario != null ? usuario.getId() : null) +
                ", bodegaOrigen=" +
                (bodegaOrigen != null
                        ? bodegaOrigen.getId()
                        : null) +
                ", bodegaDestino=" +
                (bodegaDestino != null
                        ? bodegaDestino.getId()
                        : null);

    }
}
