package ar.utn.credicoop.productopersonalizado.domain;

import java.util.UUID;

public class GenerarCodigoUnico {

  static public String GenerarCodigo(){
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

}
