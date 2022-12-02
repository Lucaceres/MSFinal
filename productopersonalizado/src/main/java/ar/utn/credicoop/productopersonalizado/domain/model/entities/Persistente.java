package ar.utn.credicoop.productopersonalizado.domain.model.entities;

import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import static javax.persistence.GenerationType.IDENTITY;

@MappedSuperclass
@Getter
public abstract class Persistente {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
}
