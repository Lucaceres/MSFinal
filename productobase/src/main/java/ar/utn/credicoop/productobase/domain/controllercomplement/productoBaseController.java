package ar.utn.credicoop.productobase.domain.controllercomplement;

import ar.utn.credicoop.productobase.domain.DTOs.*;
import ar.utn.credicoop.productobase.domain.model.entities.Area;
import ar.utn.credicoop.productobase.domain.model.entities.PosiblePersonalizacion;
import ar.utn.credicoop.productobase.domain.model.entities.ProductoBase;
import ar.utn.credicoop.productobase.domain.model.entities.TipoPersonalizacion;
import ar.utn.credicoop.productobase.domain.repositories.RepoArea;
import ar.utn.credicoop.productobase.domain.repositories.RepoProductoBase;
import ar.utn.credicoop.productobase.domain.repositories.RepoTIpoPersonalizacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RepositoryRestController
public class productoBaseController {
    @Autowired
    RepoProductoBase repoProductoBase;

    @Autowired
    RepoArea repoArea;

    @Autowired
    RepoTIpoPersonalizacion repoTipoPersonalizacion;




    @Transactional
    @DeleteMapping("/productobase/{id}")
    public @ResponseBody ResponseEntity<Object> delete(@PathVariable("id") Integer id) {

        Optional<ProductoBase> productoBaseOptional = repoProductoBase.findById(id);

        //VALIDACIONES

        if(productoBaseOptional.isPresent()) {
            ProductoBase producto = productoBaseOptional.get();
            repoProductoBase.deleteById(id);

            return ResponseEntity.ok("producto borrado");
        }
        return new ResponseEntity<Object>("El producto no existe", HttpStatus.NOT_FOUND);

    }

    @Transactional
    @PostMapping("/productobase")
    public @ResponseBody ResponseEntity<Object> create(@RequestBody ProductoBaseDTO productoBaseDTO) {

        //1.0) Obtererner las AreaPorProductoBaseDTO
        List<AreaPorProductoBaseDTO> areaPorProductoBaseDTOList = productoBaseDTO.getAreasPorProductoBase();

        // VERIFICO QUE TODAS LAS AREAS INGRESADAS Y TIPOS DE PERSONALIZACION EXISTEN

        List<Integer> areasIngresadasId = areaPorProductoBaseDTOList.stream().map(areaPorProductoBaseDTO -> areaPorProductoBaseDTO.getAreaId()).collect(Collectors.toList());
        List<Optional<Area>> areasIngresadas = areasIngresadasId.stream().map(areaIngresadasId -> repoArea.findById(areaIngresadasId)).collect(Collectors.toList());

        List<List<Integer>> tiposPersonalizacionId = areaPorProductoBaseDTOList.stream().map(areaPorProductoBaseDTO -> areaPorProductoBaseDTO.getTiposPersonalizacionId()).collect(Collectors.toList());
        List<List<Optional<TipoPersonalizacion>>> tiposIngresados = tiposPersonalizacionId.stream().map(lista -> lista.stream().map(id -> repoTipoPersonalizacion.findById(id)).collect(Collectors.toList())).collect(Collectors.toList());

        if (areasIngresadas.stream().allMatch(area -> area.isPresent())) {

            if (tiposIngresados.stream().allMatch(listaOptional -> listaOptional.stream().allMatch(optional -> optional.isPresent()))) {


                //1.1) Por cada AreaPorProductoBaseDTO utilizamos su atributo INTEGER area para buscar el area por ID

                //1.1.1) BUCLE POR CADA AREA POR PRODUCTO BASE

                List<PosiblePersonalizacion> posiblePersonalizacions = areaPorProductoBaseDTOList.stream().map(area -> {

                            //BUSCO Y SETEO EL AREA
                            PosiblePersonalizacion posiblePersonalizacion = new PosiblePersonalizacion();
                            Optional<Area> areaAAgregar = repoArea.findById(area.getAreaId());

                            posiblePersonalizacion.setArea(areaAAgregar.get());

                            //Una vez obtenida el area, Busco todos los tipos de personalizacion que le perteneccen
                            //Al AreaPorProductoBase

                            area.getTiposPersonalizacionId().forEach(tipo -> {

                                        Optional<TipoPersonalizacion> tipoPersonalizacionAAgregar = repoTipoPersonalizacion.findById(tipo);
                                        posiblePersonalizacion.agregarTipoPersonalizacion(tipoPersonalizacionAAgregar.get());

                                        // else new ResponseEntity<Object>("El tipo de personalizacion no existe", HttpStatus.NOT_FOUND);
                                    }
                            );
                            return posiblePersonalizacion;
                        }
                ).collect(Collectors.toList());

                //2)SETEAR TOD0

                ProductoBase productoBase = new ProductoBase(productoBaseDTO.getNombre(),
                        productoBaseDTO.getPrecioBase(), productoBaseDTO.getDescripcion(),
                        productoBaseDTO.getTiempoFabricacion(), posiblePersonalizacions);

                //3) Hacer el save en el repo

                repoProductoBase.save(productoBase);

                return new ResponseEntity<Object>("El producto base fue creado con éxito", HttpStatus.CREATED);
            }
            else return new ResponseEntity<Object>("El tipo de personalizacion no existe", HttpStatus.NOT_FOUND);
        }
        else return new ResponseEntity<Object>("El area no existe", HttpStatus.NOT_FOUND);
    }





}
