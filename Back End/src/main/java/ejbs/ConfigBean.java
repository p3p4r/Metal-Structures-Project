package ejbs;

import entities.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton(name = "ConfigEJB")
@Startup//EJB will be automatically instantiated once the application is
public class ConfigBean {
    @EJB
    ClientBean clinteB;
    @EJB
    SupplierBean supplierB;
    @EJB
    DraftsmanBean draftsmanB;
    @EJB
    FamilyBean familyB;
    /*@EJB
    FileBean fileB;*/
    @EJB
    MaterialBean materialBean;
    MaterialState materiaStatus;
    @EJB
    ProjectBean projectB;

    ProjectStatus status;
    //Todo: Resistance/Section caracteristicas das Variantes
    /**
     * ResistanceBean resistanceB;
     * SectionBean sectionB;
     */
    @EJB
    StructureBean structureB;
    @EJB
    TypeBean typeB;

    TypeOfAdjudication typeOfAdjudication;

    @EJB
    VarianteBean varianteBean;

    // Pay attention to the correct import: import java.util.logging.Logger;
    private static final Logger logger = Logger.getLogger("ejbs.ConfigBean");

    @PostConstruct
    public void populateDB(){
        try{
            System.out.println("##### Server Started! ##### ");

            System.out.println("##### CREATING TYPES ##### ");
            typeB.create("Perfis Enformados a Frio","Perfil para Estrutura");
            typeB.create("Chapa Perfilada","");
            typeB.create("Laje Mista","");

            System.out.println("##### CREATING FAMILIES ##### ");
            familyB.create("Ω","omega");
            familyB.create("C","z");
            familyB.create("Q","q");

            System.out.println("##### CREATING CONTACT_PERSON ##### ");

            System.out.println("##### CREATING CLIENTS ##### ");
            clinteB.create("testesss","a12312321","Teste","adress streen n","123456789", "teste@teste.pt");
            clinteB.create("user2","user2","user2","adress streen nuser2","993456713", "user2@user2.pt");
            clinteB.create("test","123","test1","adress streen nuser3","893456713", "test@test.pt");

            System.out.println("##### CREATING SUPPLIERS ##### ");
            supplierB.create("supplier","123","Supplier DLH","Leiria Street nº1","321654987", "geral@supplier.pt");
            supplierB.create("Chapas lda","1234456","Chapas lda","Leiria Street nº2","321654987", "chapas@supplier.pt");

            System.out.println("##### CREATING DRAFTSMEN ##### ");
            draftsmanB.create("draftsman","123","Draftsman","Lisboa Street nº1","528459685", "geral@draftsman.pt");
            draftsmanB.create("draftsman2","draftsman222","Draftsman CRT","Leiria Street nº1","528963963", "geral@draftsman2.pt");

            System.out.println("##### CREATING PROJECTS ##### ");
            projectB.create("Project XYS","test","draftsman2",status.ON_APPROVAL);
            projectB.create("ABC","user2","draftsman",status.DRAFT);
            projectB.create("VVVV","testesss","draftsman",status.DRAFT);
            projectB.create("Project test","test","draftsman",status.ON_APPROVAL);
            projectB.create("test","test","draftsman",status.DRAFT);

            System.out.println("##### CREATING STRUCTURES ##### ");

            structureB.create("porta","10","5","Laje Mista","Project XYS","draftsman");
            structureB.create("janela","10","5","Laje Mista","Project test","draftsman");
            structureB.create("arroz","10","5","Laje Mista","Project test","draftsman");
            structureB.create("arame","12","6","Chapa Perfilada","test","draftsman");


            System.out.println("##### CREATING FILES ##### ");
            //TODO: CREATING FILES

            System.out.println("##### CREATING ADJUDICATIONS ##### ");
            //adjudicationB.create(1,typeOfAdjudication.OnApproval,"","testesss",1);
            //adjudicationB.create(1,"testesss",1);


            //Dados da simulação
            System.out.println("####### CREATING PRODUCTS #####");
            materialBean.create("Section C 220 BF","Ω","Perfis Enformados a Frio","supplier",materiaStatus.visible);
            materialBean.create("Section Z 220 BF","Ω","Perfis Enformados a Frio","supplier",materiaStatus.visible);
            materialBean.create("Section Z 220 BF","Ω","Laje Mista","supplier",materiaStatus.hidden);

            List <Material> materials = materialBean.getAllMaterials();

            System.out.println("####### CREATING VARIANTES #####");
            //PODE LER-SE OS VALORES DOS PRODUTOS/VARIANTES DE EXCELS OU CSVs (ver excels fornecidos)
            //Exemplo básico de adição de variantes "à mão"

            varianteBean.create(1, (int)materials.get(0).getId(), "C 120/50/21 x 1.5", 13846, 13846, 375, 220000);
            varianteBean.create(2, (int)materials.get(1).getId(), "Z 120/60/13 x 2.0", 18738, 18738, 500, 220000);
            //adjudicationB.enrolAdjudicationInlVariante(1,1);

            //PODE LER-SE OS VALORES mcr_p E mcr_n A PARTIR DE UM EXCEL OU CSV (ver excels fornecidos para os produtos Perfil C e Z, que têm os valores mcr)
            //Exemplo básico de adição de valores mcr "à mão"
            Variante variante1 = varianteBean.getVariante(1);
            variante1.addMcr_p(3.0,243.2123113);
            variante1.addMcr_p(4.0,145.238784);
            variante1.addMcr_p(5.0,99.15039028);
            variante1.addMcr_p(6.0,73.71351699);
            variante1.addMcr_p(7.0,58.07716688);
            variante1.addMcr_p(8.0,47.68885195);
            variante1.addMcr_p(9.0,40.37070843);
            variante1.addMcr_p(10.0,34.9747033);
            variante1.addMcr_p(11.0,30.84866055);
            variante1.addMcr_p(12.0,27.59984422);

            //Válido para variantes simétricas, em que os mcr_p são iguais aos mcr_n
            variante1.setMcr_n((LinkedHashMap<Double, Double>) variante1.getMcr_p().clone());

            Variante variante2 = varianteBean.getVariante(2);
            variante2.addMcr_p(3.0,393.1408237);
            variante2.addMcr_p(4.0,241.9157907);
            variante2.addMcr_p(5.0,169.7815504);
            variante2.addMcr_p(6.0,129.3561949);
            variante2.addMcr_p(7.0,104.0782202);
            variante2.addMcr_p(8.0,86.9803928);
            variante2.addMcr_p(9.0,74.71876195);
            variante2.addMcr_p(10.0,65.52224563);
            variante2.addMcr_p(11.0,58.37786338);
            variante2.addMcr_p(12.0,52.65428332);

            //Válido para variantes de geometria simétrica, em que os mcr_p são iguais aos mcr_n
            variante2.setMcr_n((LinkedHashMap<Double, Double>) variante2.getMcr_p().clone());

            System.out.printf("##### ADD VARIANTES TO STRUCTURES ##### ");
            List<Integer> a = Arrays.asList(1);
            structureB.enrollListVariantsInStructure(1, a);
            /*List<Integer> b = Arrays.asList(1);
            structureB.enrollAdjudicacaoInStructure(1, b);*/

            System.out.printf("##### FINISHED() ##### ");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

}
