/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plh24_ge3;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "PRIZECATEGORIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prizecategories.findAll", query = "SELECT p FROM Prizecategories p")
    , @NamedQuery(name = "Prizecategories.findById", query = "SELECT p FROM Prizecategories p WHERE p.id = :id")
    , @NamedQuery(name = "Prizecategories.findByPrizeid", query = "SELECT p FROM Prizecategories p WHERE p.prizeid = :prizeid")
    , @NamedQuery(name = "Prizecategories.findByDivedent", query = "SELECT p FROM Prizecategories p WHERE p.divedent = :divedent")
    , @NamedQuery(name = "Prizecategories.findByWinners", query = "SELECT p FROM Prizecategories p WHERE p.winners = :winners")
    , @NamedQuery(name = "Prizecategories.findByDist", query = "SELECT p FROM Prizecategories p WHERE p.dist = :dist")
    , @NamedQuery(name = "Prizecategories.findByJackpot", query = "SELECT p FROM Prizecategories p WHERE p.jackpot = :jackpot")
    //NamedQuery για αναζήτηση κατηγοριών κερδών με βάση το id της κλήρωσης
    , @NamedQuery(name = "Prizecategories.findByDrawId", query = "SELECT p FROM Prizecategories p WHERE p.drawid = :drawid")})

public class Prizecategories implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PRIZEID")
    private Integer prizeid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DIVEDENT")
    private Double divedent;
    @Column(name = "WINNERS")
    private Integer winners;
    @Column(name = "DIST")
    private Double dist;
    @Column(name = "JACKPOT")
    private Double jackpot;
    @JoinColumn(name = "DRAWID", referencedColumnName = "DRAWID")
    @ManyToOne(optional = false)
    private Game drawid;

    public Prizecategories() {
    }

    public Prizecategories(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrizeid() {
        return prizeid;
    }

    public void setPrizeid(Integer prizeid) {
        this.prizeid = prizeid;
    }

    public Double getDivedent() {
        return divedent;
    }

    public void setDivedent(Double divedent) {
        this.divedent = divedent;
    }

    public Integer getWinners() {
        return winners;
    }

    public void setWinners(Integer winners) {
        this.winners = winners;
    }

    public Double getDist() {
        return dist;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }

    public Double getJackpot() {
        return jackpot;
    }

    public void setJackpot(Double jackpot) {
        this.jackpot = jackpot;
    }

    public Game getDrawid() {
        return drawid;
    }

    public void setDrawid(Game drawid) {
        this.drawid = drawid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prizecategories)) {
            return false;
        }
        Prizecategories other = (Prizecategories) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "plh24_ge3.Prizecategories[ id=" + id + " ]";
    }

}
