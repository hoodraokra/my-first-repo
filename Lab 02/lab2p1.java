import java.util.ArrayList;

public class lab2p1

    {
    public static ArrayList<String> DNAToCodons(String dna) {
        ArrayList<String> codons = new ArrayList<>();
        for (int i = 0; i + 3 <= dna.length(); i += 3) {
            codons.add(dna.substring(i, i + 3));
        }
        return codons;
    }

    public static String CodonToAminoAcid(String codon)
    {
        switch (codon) 
        {
            case "TTT": case "TTC": 
            return "F";

            case "TTA": case "TTG": case "CTT": case "CTC": case "CTA": case "CTG": 
            return "L";

            case "ATT": case "ATC": case "ATA": 
            return "I";

            case "ATG": 
            return "M";

            case "GTT": case "GTC": case "GTA": case "GTG": 
            return "V";

            case "TCT": case "TCC": case "TCA": case "TCG": case "GGC":
            case "GGA": case "GGG": case "AGT": case "AGC":
            return "S";

            case "CCT": case "CCC": case "CCA": case "CCG": 
            return "P";

            case "ACT": case "ACC": case "ACA": case "ACG": 
            return "T";

            case "GCT": case "GCC": case "GCA": case "GCG": 
            return "A";

            case "TAT": case "TAC": 
            return "Y";

            case "TAA": case "TAG": case "TGA": 
            return "Stop";

            case "CAT": case "CAC": 
            return "H";

            case "CAA": case "CAG": 
            return "Q";

            case "AAT": case "AAC": 
            return "N";

            case "AAA": case "AAG": 
            return "K";

            case "GAT": case "GAC": case "GAA": case "GAG": 
            return "D";

            case "TGT": case "TGC": 
            return "C";

            case "TGG": 
            return "W";

            case "CGT": case "CGC": case "CGA": case "CGG": case "AGA": case "AGG": 
            return "R";

            case "GGT": 
            return "G";
            
            default: return "?";
        }
    }

    public static ArrayList<String> dna_to_amino_acid(String dna) 
        {
        ArrayList<String> codons = DNAToCodons(dna);
        ArrayList<String> aminoAcids = new ArrayList<>();

        for (String codon : codons) {
            aminoAcids.add(CodonToAminoAcid(codon));
        }

        return aminoAcids;
        }

    public static boolean is_match(ArrayList<String> amino_seq1, ArrayList<String> amino_seq2) {
        return amino_seq1.equals(amino_seq2);
    }
    
    public static void main(String[] args) {
        String DNA1 = "CTGATATTGTATCCGGCCGAA";
        String DNA2 = "CTAGCCGGTGGTTATTAATAGTAAACTATTCCA";
        String DNA3 = "TTAATCCTCTACCCCGCAGAG";

        ArrayList<String> amino1 = dna_to_amino_acid(DNA1);
        ArrayList<String> amino2 = dna_to_amino_acid(DNA2);
        ArrayList<String> amino3 = dna_to_amino_acid(DNA3);

        System.out.println("Amino Acids for DNA1: " + amino1);
        System.out.println("Amino Acids for DNA2: " + amino2);
        System.out.println("Amino Acids for DNA3: " + amino3);

        if (is_match(amino1, amino2)) {
            System.out.println("DNA1 and DNA2 produce identical amino acid sequences.");
        }
        if (is_match(amino1, amino3)) {
            System.out.println("DNA1 and DNA3 produce identical amino acid sequences.");
        }
        if (is_match(amino2, amino3)) {
            System.out.println("DNA2 and DNA3 produce identical amino acid sequences.");
        }
    }
    
    }