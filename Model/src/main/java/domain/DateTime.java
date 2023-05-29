package domain;

import javax.persistence.Table;
import java.io.Serializable;

@javax.persistence.Entity
@Table( name = "Date" )
public class DateTime extends domain.Entity<Integer> implements Serializable {
    private Integer zi;
    private Integer luna;
    private Integer an;
    private Integer ora;
    private Integer minut;

    public DateTime(Integer integer, Integer zi, Integer luna, Integer an, Integer ora, Integer minut) {
        super(integer);
        this.zi = zi;
        this.luna = luna;
        this.an = an;
        this.ora = ora;
        this.minut = minut;
    }


    public DateTime() {

    }

    public Integer getZi() {
        return zi;
    }

    public void setZi(Integer zi) {
        this.zi = zi;
    }

    public Integer getLuna() {
        return luna;
    }

    public void setLuna(Integer luna) {
        this.luna = luna;
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Integer getOra() {
        return ora;
    }

    public void setOra(Integer ora) {
        this.ora = ora;
    }

    public Integer getMinut() {
        return minut;
    }

    public void setMinut(Integer minut) {
        this.minut = minut;
    }

    @Override
    public String toString() {
        return "DateTime{" +
                "zi=" + zi +
                ", luna=" + luna +
                ", an=" + an +
                ", ora=" + ora +
                ", minut=" + minut +
                '}';
    }
}
