package ru.itis.cloudphoto.commads.validation;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;

public class PathValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        File file = new File(value);
        if (!file.exists() || !file.isDirectory()) {
            throw new ParameterException(String.format("%s is not suitable", value));
        }
    }
}
