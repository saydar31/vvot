package ru.itis.cloudphoto.commads;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.itis.cloudphoto.commads.validation.PathValidator;

import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoadArgs {

    @Parameter
    private String command;

    @Parameter(names = "-p", converter = FileConverter.class, validateWith = PathValidator.class)
    private File path;
    @Parameter(names = "-a", required = true)
    private String album;
}
