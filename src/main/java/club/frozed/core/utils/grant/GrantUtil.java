package club.frozed.core.utils.grant;

import club.frozed.core.manager.player.grants.Grant;
import club.frozed.core.utils.lang.Lang;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 22/09/2020 @ 08:26
 */

public class GrantUtil {
    public static String grantsToBase64(List<Grant> grants) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(grants.size());
            for (Grant grant : grants) {
                dataOutput.writeObject(grant.getRank());
                dataOutput.writeObject(Long.valueOf(grant.getAddedBy()));
                dataOutput.writeObject(Long.valueOf(grant.getDuration()));
                dataOutput.writeObject(Long.valueOf(grant.getRemovedBy()));
                dataOutput.writeObject(grant.getAddedBy());
                dataOutput.writeObject(grant.getReason());
                dataOutput.writeObject(grant.getRemovedBy());
                dataOutput.writeObject(Boolean.valueOf(grant.isActive()));
                dataOutput.writeObject(Boolean.valueOf(grant.isPermanent()));
                dataOutput.writeObject(grant.getServer());
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException(Lang.PREFIX + " Error in save grants.", e);
        }
    }

    public static List<Grant> grantsFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            List<Grant> grants = new ArrayList<>();
            int size = dataInput.readInt();
            for (int i = 0; i < size; i++) {
                Grant grant = new Grant(
                        (String)dataInput.readObject(),
                        (Long) dataInput.readObject(),
                        (Long) dataInput.readObject(),
                        (Long) dataInput.readObject(),
                        (String)dataInput.readObject(),
                        (String)dataInput.readObject(),
                        (String)dataInput.readObject(),
                        (Boolean) dataInput.readObject(),
                        (Boolean) dataInput.readObject(),
                        (String)dataInput.readObject());
                grants.add(grant);
            }
            dataInput.close();
            return grants;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
