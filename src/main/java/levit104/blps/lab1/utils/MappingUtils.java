package levit104.blps.lab1.utils;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MappingUtils {
    private final ModelMapper modelMapper;

    public <S, D> D mapObject(S sourceObject, Class<D> destinationClass) {
        return modelMapper.map(sourceObject, destinationClass);
    }

    public <S, D> List<D> mapList(List<S> sourceObjects, Class<D> destinationClass) {
        return sourceObjects.stream().map(s -> mapObject(s, destinationClass)).toList();
    }
}
