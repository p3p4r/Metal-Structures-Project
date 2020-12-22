package entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllVariants",
                query = "SELECT v FROM Variante v ORDER BY v.codigo" // JPQL
        )
})
public class Variante {

    private static double G = 78.5;

    @Id
    private int codigo;

    @ManyToOne
    @JoinColumn(name="NOME_MATERIAL")
    @NotNull
    private Material material;

    @ManyToMany
    @JoinTable(name = "VARIANTE_STRUCTURE",
        joinColumns = @JoinColumn(name = "VARIANTE_ID", referencedColumnName = "CODIGO"),
        inverseJoinColumns = @JoinColumn(name = "STRUCTURE_ID", referencedColumnName = "ID"))
    private List<Structure> structures;

    @ManyToMany
    @JoinTable(name = "VARIANTE_ADJUDICATION",
        joinColumns = @JoinColumn(name = "VARIANTE_ID", referencedColumnName = "CODIGO"),
        inverseJoinColumns = @JoinColumn(name = "ADJUDICATION_ID", referencedColumnName = "ID"))
    private List<Adjudication> adjudications;

    @ManyToMany
    @JoinTable(name = "SUPPLIER_ADJUDICATION",
            joinColumns = @JoinColumn(name = "VARIANTE_ID", referencedColumnName = "CODIGO"),
            inverseJoinColumns = @JoinColumn(name = "SUPPLIER_ID", referencedColumnName = "ID"))
    private List<Supplier> suppliers;

    @NotNull
    private String nome;

    private double weff_p;
    private double weff_n;
    private double ar;
    private double sigmaC;
    private double pp;

    @Lob
    private LinkedHashMap<Double,Double> mcr_p;
    @Lob
    private LinkedHashMap<Double,Double> mcr_n;

    //Todo: Caracteristicas
    //private List<Section> sections;
    //private List<Resistance> resistances;

    public Variante(){
        this.mcr_p = new LinkedHashMap<Double,Double>();
        this.mcr_n = new LinkedHashMap<Double,Double>();
        this.structures = new LinkedList<Structure>();
        this.adjudications = new LinkedList<Adjudication>();
        this.suppliers = new LinkedList<>();
    }

    public Variante(int codigo, @NotNull Material material, @NotNull String nome, double weff_p, double weff_n, double ar, double sigmaC) {
        this.codigo = codigo;
        this.material = material;
        this.nome = nome;
        this.weff_p = weff_p;
        this.weff_n = weff_n;
        this.ar = ar;
        this.sigmaC = sigmaC;
        this.pp = G * ar * Math.pow(10, -6);
        this.mcr_p = new LinkedHashMap<Double,Double>();
        this.mcr_n = new LinkedHashMap<Double,Double>();
        this.structures = new LinkedList<Structure>();
        this.adjudications = new LinkedList<Adjudication>();
        this.suppliers = new LinkedList<Supplier>();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int code) {
        this.codigo = code;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String name) {
        this.nome = name;
    }

    public double getWeff_p() {
        return weff_p;
    }

    public void setWeff_p(double weff_p) {
        this.weff_p = weff_p;
    }

    public double getWeff_n() {
        return weff_n;
    }

    public void setWeff_n(double weff_n) {
        this.weff_n = weff_n;
    }

    public double getAr() {
        return ar;
    }

    public void setAr(double ar) {
        this.ar = ar;
    }

    public double getSigmaC() {
        return sigmaC;
    }

    public void setSigmaC(double sigmaC) {
        this.sigmaC = sigmaC;
    }

    public double getPp() {
        return pp;
    }

    public void setPp(double pp) {
        this.pp = pp;
    }

    public LinkedHashMap<Double, Double> getMcr_p() {
        return mcr_p;
    }

    public void setMcr_p(LinkedHashMap<Double, Double> mcr_p) {
        this.mcr_p = mcr_p;
    }

    public void addMcr_p(Double L, Double mcr_pValue){
        mcr_p.put(L, mcr_pValue);
    }

    public void removeMcr_p(Double LToRemove){
        mcr_p.remove(LToRemove);
    }

    public LinkedHashMap<Double, Double> getMcr_n() {
        return mcr_n;
    }

    public void setMcr_n(LinkedHashMap<Double, Double> mcr_n) {
        this.mcr_n = mcr_n;
    }

    public void addMcr_n(Double L, Double mcr_nValue){
        mcr_n.put(L, mcr_nValue);
    }

    public void removeMcr_n(Double LToRemove){
        mcr_n.remove(LToRemove);
    }

    public static double getG() {
        return G;
    }

    public static void setG(double g) {
        G = g;
    }

    public List<Structure> getStructures() {
        return structures;
    }

    public void setStructures(List<Structure> structures) {
        this.structures = structures;
    }

    public List<Adjudication> getAdjudications() {
        return adjudications;
    }

    public void setAdjudications(List<Adjudication> adjudications) {
        this.adjudications = adjudications;
    }

    public void addAdjudication(Adjudication adjudication) {
        adjudications.add(adjudication);
    }

    public void removeAdjudication(Adjudication adjudication) {
        adjudications.remove(adjudication);
    }
    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public void addStructure(Structure s) {
        structures.add(s);
    }

    public void addSupplier(Supplier s) {
        this.suppliers.add(s);
    }

    public void removeSupplier(Supplier s) {
        this.suppliers.remove(s);
    }
}
