package ejbs;

import entities.Variante;
import exceptions.MyEntityNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class SimulacaoBean {
    @PersistenceContext
    EntityManager em;

    private static double EE = 210000000.0;
    private static double alfaLT = 0.21;

    public List getSimulationMaterials(int nb, double LVao, int q, int variantCode,String name) throws MyEntityNotFoundException {
        // Later change to be able two choose 2 or more -> checkbox
        List materials = new LinkedList();

        List<Variante> materialVarianteList;
        if (name.equals("all")){
            materialVarianteList = em.createQuery("SELECT v FROM Variante v WHERE v.material.family.name is not null")
                    //.setParameter("materialNome", name)
                    //.setMaxResults(10)
                    .getResultList();
        }else{
            materialVarianteList = em.createQuery("SELECT v FROM Variante v WHERE v.material.family.name LIKE :materialNome")
                    .setParameter("materialNome", name)
                    //.setMaxResults(10)
                    .getResultList();
        }

        for (Variante v : materialVarianteList) {
            if(variantCode == 0)
                variantCode = v.getCodigo();

            // Add to list if material is approved
            if(simulaVariante(nb,LVao,q,variantCode))
                materials.add(v.getMaterial());
        }

        //if (simulaVariante(nb,Lvao,q,materialList.))
        return materials;
    }

    public List getSimulationVariants(int nb, double LVao, int q, int variantCode,String name) throws MyEntityNotFoundException {

        List<Variante> materialVarianteList;
        if (name.equals("all")){
            materialVarianteList = em.createQuery("SELECT v FROM Variante v WHERE v.material.family.name is not null")
                    //.setParameter("materialNome", name)
                    //.setMaxResults(10)
                    .getResultList();
        }else{
            materialVarianteList = em.createQuery("SELECT v FROM Variante v WHERE v.material.family.name LIKE :materialNome")
                    .setParameter("materialNome", name)
                    //.setMaxResults(10)
                    .getResultList();
        }

        return materialVarianteList;
    }

    // - nb é o número de barras/vãos;
    // - LVao é a largura de cada vão (sempre igual entre os vários vãos);
    // - q é a carga total a que a estrutura estará sujeita.
    public boolean simulaVariante(int nb, double LVao, int q, int variantCode) throws MyEntityNotFoundException {
        Variante variante = em.find(Variante.class, variantCode);

        if(variante == null)  throw new MyEntityNotFoundException("Variante with code: " + variantCode + " does not exists");

        boolean seguro = true;

        double[] msd = momentosFletoresAtuantes(nb, LVao, q, variante);

        double lambda1 = Math.PI * Math.sqrt(EE / variante.getSigmaC());
        double mrd_p = momentoResistenteProduto(lambda1, variante.getWeff_p(), variante.getMcr_p().get(LVao));
        double mrd_n = momentoResistenteProduto(lambda1, variante.getWeff_n(), variante.getMcr_n().get(LVao));

        for (int i = 0; i < msd.length; i++) {
            double rs;
            if(msd[i] >= 0 ){
                rs = Math.abs(msd[i]) / Math.abs(mrd_p);
            }else{
                rs = Math.abs(msd[i]) / Math.abs(mrd_n);
            }
            if(rs >= 1){
               //System.out.println("Não verifica segurança na secção " + i + " da variante " + variante.getNome() + " do material " + variante.getMaterial().getName());
                seguro = false;
            }
        }
        return seguro;
    }


    private double[] momentosFletoresAtuantes(int nb, double L, int q, Variante variante){

        double qt = q + variante.getPp();

        int ns = 2 * nb - 1;

        double[] msd = new double[ns];

        double qt_times_L_times_L = qt * L * L;

        switch(nb){
            case 1:
                msd[0] = 1.0 / 8.0 * qt_times_L_times_L;
                break;
            case 2:
                msd[0] = msd[2] = 9.0 / 128.0 * qt_times_L_times_L;
                msd[1] = 1.0 / 8.0 * qt_times_L_times_L;
                break;
            case 3:
                msd[0] = msd[4] = 2.0 / 25.0 * qt_times_L_times_L;
                msd[1] = msd[3] = - 1.0 / 10.0 * qt_times_L_times_L;
                msd[2] = 1.0 / 40.0 * qt_times_L_times_L;
                break;
            case 4:
                msd[0] = msd[6] = 121.0 / 1568.0 * qt_times_L_times_L;
                msd[1] = msd[5] = - 3.0 / 28.0 * qt_times_L_times_L;
                msd[2] = msd[4] = 57.0 / 1568.0 * qt_times_L_times_L;
                msd[3] = - 1.0 / 14.0 * qt_times_L_times_L;
        }

        return msd;
    }

    private double momentoResistenteProduto(double lambda1, double weff, double mcr) {

        double lambdaLT = Math.PI * Math.sqrt(EE * weff / mcr);
        double lambdaLTb = lambdaLT / lambda1;
        double tetaLT = alfaLT * (lambdaLTb - 0.2);
        double phiLT = (lambdaLTb * lambdaLTb + tetaLT + 1) / 2.0;
        double chiLT = (phiLT - Math.sqrt(phiLT * phiLT - lambdaLTb * lambdaLTb)) / (lambdaLTb * lambdaLTb);

        double mrd = chiLT * EE * weff;

        return mrd;
    }

}
