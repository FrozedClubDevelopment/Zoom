package club.frozed.core.manager.punishments;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Punishment {

    @Getter public static List<Punishment> punishments = new ArrayList<>();

    private String type, punished, punisher, date;

}
