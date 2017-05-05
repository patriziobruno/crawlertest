package net.dstc.crawlertest.rest;

import java.util.List;
import java.util.stream.Collectors;
import net.dstc.crawlertest.data.Data;
import net.dstc.crawlertest.data.Optimizable;
import net.dstc.crawlertest.rest.model.OptimizableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.modelmapper.ModelMapper;

/**
 * This controller exposes REST endpoints to query indexed data about optimizable
 * sites.
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
@RestController("/data")
public class DataController {

    @Autowired
    private Data data;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/list/{uuid}")
    public List<OptimizableDTO> list(@PathVariable String uuid) {
        return data.list(uuid)
                .stream()
                .map(optimizable -> convertToDto(optimizable))
                .collect(Collectors.toList());
    }

    @GetMapping("/list")
    public List<OptimizableDTO> listAll() {
        return data.list(null)
                .stream()
                .map(optimizable -> convertToDto(optimizable))
                .collect(Collectors.toList());
    }

    private OptimizableDTO convertToDto(Optimizable entity) {
        OptimizableDTO dto = modelMapper.map(entity, OptimizableDTO.class);
        return dto;
    }
}
