package correcter;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

//        encode();
//        send();
//        decode();
        System.out.print("Write a mode:");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        if (input.equals("encode")) {
            encode();
        } else if (input.equals("send")) {
            send();
        } else if (input.equals("decode" )) {
            decode();
        }
    }



    private static void encode() throws IOException {
        //System.out.println("Send.txt:");
        String[] strings;
        try (//FileInputStream in = new FileInputStream("F:\\Downloads\\Login\\Error Correcting Encoder-Decoder\\Error Correcting Encoder-Decoder\\task\\src\\correcter\\send.txt");
             //FileOutputStream out = new FileOutputStream("F:\\\\Downloads\\\\Login\\\\Error Correcting Encoder-Decoder\\\\Error Correcting Encoder-Decoder\\\\task\\\\src\\\\correcter\\\\encoded.txt")
             FileInputStream in = new FileInputStream("send.txt");
             FileOutputStream out = new FileOutputStream("encoded.txt");
        ) {
            byte[] bytes = in.readAllBytes();
            String text = "";
            for (byte b : bytes) {
                text += String.valueOf((char) b);
            }

            System.out.println("text view: " + text);
            strings = new String[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                //strings[i] = Integer.toBinaryString(bytes[i]);
                strings[i] = Integer.toBinaryString((bytes[i] & 0xFF) + 256).substring(1);
            }
            //make one giant string
            //iterate and split as we go

            System.out.print("hex view: ");
            for (byte b : bytes) {
                System.out.print(Integer.toHexString(b) + " ");
            }
            System.out.println();

            StringBuilder sb = new StringBuilder();
            System.out.print("bin view: ");
            for (String s : strings) {
                System.out.print(s + " ");
                sb.append(s);
            }

            String allBits = sb.toString();
            int length = allBits.length();

            String encodedBits = encodeFourBits(allBits);

            System.out.println(encodedBits);
//

            List<String> byteGroups = new ArrayList<>();
            //String[] chopChop= new String[superDuper.length()/8 + 1];
            int i = 0;
            while (encodedBits.length() > 0) {
                if (encodedBits.length() >= 8) {
                    byteGroups.add(encodedBits.substring(0, 8));
                    encodedBits = encodedBits.substring(8);
                    i++;
                } else {
                    byteGroups.add(encodedBits);
                    break;
                }

            }
            for (String st : byteGroups) {
                System.out.print(st + " ");
                if (st != null) {
                    out.write((byte) (int)Integer.valueOf(st, 2));
                }
            }
            System.out.println();
//todo missing 2 digits from end of file: fix
        }
    }


    private static void send() {
        try (
                //FileInputStream in = new FileInputStream("F:\\Downloads\\Login\\Error Correcting Encoder-Decoder\\Error Correcting Encoder-Decoder\\task\\src\\correcter\\encoded.txt");
                //FileOutputStream out = new FileOutputStream("F:\\Downloads\\Login\\Error Correcting Encoder-Decoder\\Error Correcting Encoder-Decoder\\task\\src\\correcter\\received.txt");
                FileInputStream in = new FileInputStream("encoded.txt");
                FileOutputStream out = new FileOutputStream("received.txt")
                ) {
            Random random = new Random();
            byte[] bytes = in.readAllBytes();
            //System.out.println("\nencoded.txt:");
            //System.out.print("hex view: ");

           // for (byte b : bytes) {
              //  System.out.print(Integer.toHexString(b) + " ");
            //}
            //System.out.print("\nbin view: ");

//            for (int i = 0; i < bytes.length; i++) {
//                System.out.print(Integer.toBinaryString((bytes[i] & 0xFF) + 256).substring(1) + " ");
//            }

            List<Byte> receivedBytes = new ArrayList<>();

            for (byte b : bytes) {
                int bitPosition = random.nextInt(8);
                out.write(changeBit(bitPosition, b));
                receivedBytes.add((byte) changeBit(bitPosition, b));
            }

            //System.out.println("\n\nreceived text: ");
            //System.out.print("bin view: ");
//            for (Byte b : receivedBytes) {
//              //  System.out.print(Integer.toBinaryString((b & 0xFF) + 256).substring(1) + " ");
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static int changeBit(int bitPosition, int target) {
        int mask = 1 << bitPosition;
        return target ^ mask;
    }

    private static void decode() {
        try (//FileInputStream in = new FileInputStream("F:\\Downloads\\Login\\Error Correcting Encoder-Decoder\\Error Correcting Encoder-Decoder\\task\\src\\correcter\\received.txt");
             //FileOutputStream out = new FileOutputStream("F:\\Downloads\\Login\\Error Correcting Encoder-Decoder\\Error Correcting Encoder-Decoder\\task\\src\\correcter\\decoded.txt");
             FileInputStream in = new FileInputStream("received.txt");
             FileOutputStream out = new FileOutputStream("decoded.txt")
        ) {

            //System.out.println("\n\nreceived.txt: ");
            byte[] bytes = in.readAllBytes();
            //System.out.print("hex view: ");
//
//            for (byte b : bytes) {
//              //  System.out.print(Integer.toHexString(b) + " ");
//            }
//            //System.out.print("\nbin view: ");
//
//            for (int i = 0; i < bytes.length; i++) {
//              //  System.out.print(Integer.toBinaryString((bytes[i] & 0xFF) + 256).substring(1) + " ");
//            }



            //System.out.println("\ndecoded.txt:");

            String[] strings = new String[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                //strings[i] = Integer.toBinaryString(bytes[i]);
                strings[i] = Integer.toBinaryString((bytes[i] & 0xFF) + 256).substring(1);
            }

            StringBuilder sb = new StringBuilder();
            for (String s : strings) {
                String d3 = s.substring(2, 3);
                String d5 = s.substring(4, 5);
                String d6 = s.substring(5, 6);
                String d7 = s.substring(6, 7);
                String p1 = s.substring(0, 1);
                String p2 = s.substring(1, 2);
                String p4 = s.substring(3, 4);
                boolean p1Correct;
                boolean p2Correct;
                boolean p4Correct;
                int incorrectBit = 0;

                if ((Integer.parseInt(d3) + Integer.parseInt(d5) + Integer.parseInt(d7)) % 2 == Integer.parseInt(p1)) {
                    p1Correct = true;
                } else {
                    p1Correct = false;
                }
                if ((Integer.parseInt(d3) + Integer.parseInt(d6) + Integer.parseInt(d7)) % 2 == Integer.parseInt(p2)) {
                    p2Correct = true;
                } else {
                    p2Correct = false;
                }

                if ((Integer.parseInt(d5) + Integer.parseInt(d6) + Integer.parseInt(d7)) % 2 == Integer.parseInt(p4)) {
                    p4Correct = true;
                } else {
                    p4Correct = false;
                }

                if (!p1Correct && !p2Correct && !p4Correct) {
                    incorrectBit = 7;
                } else if (!p1Correct && ! p2Correct) {
                    incorrectBit = 3;
                } else if (!p1Correct && !p4Correct) {
                    incorrectBit = 5;
                } else if (!p2Correct && ! p4Correct) {
                    incorrectBit = 6;
                } else if (!p1Correct) {
                    incorrectBit = 1;
                } else if (!p2Correct) {
                    incorrectBit = 2;
                } else if (!p4Correct) {
                    incorrectBit = 4;
                }

                switch (incorrectBit) {
                    case 1:
                        if (p1.equals("0")) {
                            p1 = "1";
                        } else {
                            p1 = "0";
                        }
                        break;
                    case 2:
                        if (p2.equals("0")) {
                            p2 = "1";
                        } else {
                            p2 = "0";
                        }
                        break;
                    case 3:
                        if (d3.equals("0")) {
                            d3 = "1";
                        } else {
                            d3 = "0";
                        }
                        break;
                    case 4:
                        if (p4.equals("0")) {
                            p4 = "1";
                        } else {
                            p4 = "0";
                        }
                        break;
                    case 5:
                        if (d5.equals("0")) {
                            d5 = "1";
                        } else {
                            d5 = "0";
                        }
                        break;
                    case 6:
                        if (d6.equals("0")) {
                            d6 = "1";
                        } else {
                            d6 = "0";
                        }
                        break;
                    case 7:
                        if (d7.equals("0")) {
                            d7 = "1";
                        } else {
                            d7 = "0";
                        }
                        break;
                }

                sb.append(d3).append(d5).append(d6).append(d7);
                }


            String decodedBits = sb.toString();
            //System.out.println(decodedBits);

            List<String> decodedBytes = new ArrayList<>();

            int i = 0;
            while (decodedBits.length() > 0) {
                if (decodedBits.length() >= 8) {
                    decodedBytes.add(decodedBits.substring(0, 8));
                    decodedBits = decodedBits.substring(8);
                    i++;
                } else {
                    decodedBytes.add(decodedBits);
                    break;
                }
            }

            //System.out.println();
            for (String s : decodedBytes) {
                System.out.print(s + " ");
                out.write((byte) (int)Integer.valueOf(s, 2));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String encodeFourBits(String input) {
        StringBuilder sb = new StringBuilder();
        while (input.length() > 0) {

            if (input.length() >= 4) {
                String bit1 = input.substring(0, 1);
                String bit2 = input.substring(1, 2);
                String bit3 = input.substring(2, 3);
                String bit4 = input.substring(3, 4);
                String p1;
                String p2;
                String p3;

                if ((Integer.parseInt(bit1) + Integer.parseInt(bit2) + Integer.parseInt(bit4)) % 2 == 0) {
                    p1 = "0";
                } else {
                    p1 = "1";
                }
                if ((Integer.parseInt(bit1) + Integer.parseInt(bit3) + Integer.parseInt(bit4)) % 2 == 0) {
                    p2 = "0";
                } else {
                    p2 = "1";
                }

                if ((Integer.parseInt(bit2) + Integer.parseInt(bit3) + Integer.parseInt(bit4)) % 2 == 0) {
                    p3 = "0";
                } else {
                    p3 = "1";
                }

                sb.append(p1).append(p2).append(bit1).append(p3).append(bit2).append(bit3).append(bit4).append("0");
            }

            input = input.substring(4);



        }
        return sb.toString();
    }

}