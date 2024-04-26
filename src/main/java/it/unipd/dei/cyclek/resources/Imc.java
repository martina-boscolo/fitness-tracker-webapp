package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Imc extends AbstractResource{
    private final Integer idUser;
    private final Double userMeanImc;
    private final double imc;

    public Imc(double imc) {
        this.idUser = null;
        this.userMeanImc = null;
        this.imc = imc;
    }

    public Imc(Integer idUser, double userMeanImc, double imc)
    {
        this.idUser = idUser;
        this.userMeanImc = userMeanImc;
        this.imc = imc;
    }

    public Integer getIdUser() {return idUser;}

    public Double getUserMeanImc() {
        return userMeanImc;
    }

    public double getImc() {
        return imc;
    }


    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        if (this.getIdUser() == null) {
            String json = new ObjectMapper()
                    .enable(SerializationFeature.WRAP_ROOT_VALUE)
                    .writer().withRootName("GlobalMeanImc")
                    .writeValueAsString(this.getImc());
            out.write(json.getBytes(StandardCharsets.UTF_8));
        } else {
            String json = new ObjectMapper()
                    .enable(SerializationFeature.WRAP_ROOT_VALUE)
                    .writer().withRootName("userImc")
                    .writeValueAsString(this);
            out.write(json.getBytes(StandardCharsets.UTF_8));
        }
    }
}
