package ru.itis.cloudphoto.commads;

import com.beust.jcommander.Parameter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListArgs {
    @Parameter
    private String command;
    @Parameter(names = "-a")
    private String album;
}
