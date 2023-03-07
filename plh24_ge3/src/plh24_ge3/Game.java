/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plh24_ge3;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "GAME")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Game.findAll", query = "SELECT g FROM Game g")
    , @NamedQuery(name = "Game.findByDrawid", query = "SELECT g FROM Game g WHERE g.drawid = :drawid")
    , @NamedQuery(name = "Game.findByNum1", query = "SELECT g FROM Game g WHERE g.num1 = :num1")
    , @NamedQuery(name = "Game.findByNum2", query = "SELECT g FROM Game g WHERE g.num2 = :num2")
    , @NamedQuery(name = "Game.findByNum3", query = "SELECT g FROM Game g WHERE g.num3 = :num3")
    , @NamedQuery(name = "Game.findByNum4", query = "SELECT g FROM Game g WHERE g.num4 = :num4")
    , @NamedQuery(name = "Game.findByNum5", query = "SELECT g FROM Game g WHERE g.num5 = :num5")
    , @NamedQuery(name = "Game.findByBonus", query = "SELECT g FROM Game g WHERE g.bonus = :bonus")
    , @NamedQuery(name = "Game.findByDrawdate", query = "SELECT g FROM Game g WHERE g.drawdate = :drawdate")
    //NamedQuery για την αναζήτηση κληρώσεων σε εύρος ημερομηνιών
    , @NamedQuery(name = "Game.findBetweenDrawdate", query = "SELECT g FROM Game g WHERE g.drawdate BETWEEN :drawdateFrom AND :drawdateTo")})
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "DRAWID")
    private Integer drawid;
    @Column(name = "NUM1")
    private Integer num1;
    @Column(name = "NUM2")
    private Integer num2;
    @Column(name = "NUM3")
    private Integer num3;
    @Column(name = "NUM4")
    private Integer num4;
    @Column(name = "NUM5")
    private Integer num5;
    @Column(name = "BONUS")
    private Integer bonus;
    @Column(name = "DRAWDATE")
    @Temporal(TemporalType.DATE)
    private Date drawdate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "drawid")
    private List<Prizecategories> prizecategoriesList;

    public Game() {
    }

    public Game(Integer drawid) {
        this.drawid = drawid;
    }

    public Integer getDrawid() {
        return drawid;
    }

    public void setDrawid(Integer drawid) {
        this.drawid = drawid;
    }

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    public Integer getNum2() {
        return num2;
    }

    public void setNum2(Integer num2) {
        this.num2 = num2;
    }

    public Integer getNum3() {
        return num3;
    }

    public void setNum3(Integer num3) {
        this.num3 = num3;
    }

    public Integer getNum4() {
        return num4;
    }

    public void setNum4(Integer num4) {
        this.num4 = num4;
    }

    public Integer getNum5() {
        return num5;
    }

    public void setNum5(Integer num5) {
        this.num5 = num5;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public Date getDrawdate() {
        return drawdate;
    }

    public void setDrawdate(Date drawdate) {
        this.drawdate = drawdate;
    }

    @XmlTransient
    public List<Prizecategories> getPrizecategoriesList() {
        return prizecategoriesList;
    }

    public void setPrizecategoriesList(List<Prizecategories> prizecategoriesList) {
        this.prizecategoriesList = prizecategoriesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (drawid != null ? drawid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Game)) {
            return false;
        }
        Game other = (Game) object;
        if ((this.drawid == null && other.drawid != null) || (this.drawid != null && !this.drawid.equals(other.drawid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "plh24_ge3.Game[ drawid=" + drawid + " ]";
    }

}
