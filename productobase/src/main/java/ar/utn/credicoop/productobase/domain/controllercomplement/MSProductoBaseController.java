package ar.utn.credicoop.productobase.domain.controllercomplement;

import ar.utn.credicoop.productobase.domain.DTOs.Personalizacion;
import ar.utn.credicoop.productobase.domain.DTOs.PersonalizacionDTO;
import ar.utn.credicoop.productobase.domain.DTOs.ProductoPersonalizadoDTO;
import ar.utn.credicoop.productobase.domain.DTOs.RespuestaValidacion;
import ar.utn.credicoop.productobase.domain.model.entities.Area;
import ar.utn.credicoop.productobase.domain.model.entities.ProductoBase;
import ar.utn.credicoop.productobase.domain.model.entities.TipoPersonalizacion;
import ar.utn.credicoop.productobase.domain.repositories.RepoArea;
import ar.utn.credicoop.productobase.domain.repositories.RepoProductoBase;
import ar.utn.credicoop.productobase.domain.repositories.RepoTIpoPersonalizacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class MSProductoBaseController {

    @Autowired
    RepoProductoBase repoProductoBase;

    @Autowired
    RepoArea repoArea;

    @Autowired
    RepoTIpoPersonalizacion repoTipoPersonalizacion;


    @PostMapping("/valid")
    public  RespuestaValidacion validarProductoPersonalizado(@RequestBody ProductoPersonalizadoDTO producto)
    {
        //Obtenemos el producto base
        Optional<ProductoBase> productoBaseOptional = repoProductoBase.findById(producto.getProductoBaseId());
        List<PersonalizacionDTO> personalizacionesDTOList = producto.getPersonalizacionesDTO();
       //OBTENEMOS LAS AREAS
        List<Integer> listaDeAreaIDs = personalizacionesDTOList.stream().map(perso -> perso.getAreaId()).collect(Collectors.toList());
        List<Optional<Area>> listaDeAreas = listaDeAreaIDs.stream().map(area -> repoArea.findById(area)).collect(Collectors.toList());


        //OBTENEMOS LOS TIPOS DE PERSONALIZACION
        List<Integer> listaDeTipoDePersonalizacionIDs = personalizacionesDTOList.stream().map(perso -> perso.getTipoPersonalizacionId()).collect(Collectors.toList());
        List<Optional<TipoPersonalizacion>> listaDeTipoPersonalizacion = listaDeTipoDePersonalizacionIDs.stream().map(tipo -> repoTipoPersonalizacion.findById(tipo)).collect(Collectors.toList());

        //VALIDO SI EXISTE EL PRODUCTO BASE
        if(productoBaseOptional.isPresent())
        {
            //VALIDO SI EXISTEN TANTO EL AREA COMO TIPO DE PERSO
            if (listaDeAreas.stream().allMatch(area -> area.isPresent()) && listaDeTipoPersonalizacion.stream().allMatch(tipoPersonalizacion -> tipoPersonalizacion.isPresent()))
            {
                List<Personalizacion> personalizacionList = personalizacionesDTOList.stream().map(personalizacionDTO -> new Personalizacion(personalizacionDTO.getDescripcion(), personalizacionDTO.getPrecio(), repoTipoPersonalizacion.findById(personalizacionDTO.getTipoPersonalizacionId()).get(), repoArea.findById(personalizacionDTO.getAreaId()).get())).collect(Collectors.toList());

                if(productoBaseOptional.get().validarPersos(personalizacionList))
                {
                    return new RespuestaValidacion(true,"El producto personalizado es valido!");
                }
            }
            return new RespuestaValidacion(false,"El area o el tipo de personalizacion no existen!");
        }
        return new RespuestaValidacion(false,"Producto base no encontrado!");

    }

    @Transactional
    @PostMapping("/precio")
    public Double buscarPrecioBase(@RequestBody Integer productoBaseId)
    {
        return repoProductoBase.findById(productoBaseId).get().getPrecioBase();

    }

    @Transactional
    @PostMapping("/nombre")
    String buscarNombreProductoBase(@RequestBody Integer productoBaseId)
    {
        //NO VERIFICAMOS QUE EXISTA PORQUE YA ESTA VERIFICADO EN OTROS LADOS
        return repoProductoBase.findById(productoBaseId).get().getNombre();
    }


}
