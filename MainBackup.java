package correcter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainBackup {



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

                //System.out.println("text view: " + text);
                strings = new String[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    //strings[i] = Integer.toBinaryString(bytes[i]);
                    strings[i] = Integer.toBinaryString((bytes[i] & 0xFF) + 256).substring(1);
                }
                //make one giant string
                //iterate and split as we go

                //System.out.print("hex view: ");
                for (byte b : bytes) {
                    //  System.out.print(Integer.toHexString(b) + " ");
                }
                ///System.out.println();

                StringBuilder sb = new StringBuilder();
                //System.out.print("bin view: ");
                for (String s : strings) {
                    //  System.out.print(s + " ");
                    sb.append(s);
                }

                String superDuper = sb.toString();
                int length = superDuper.length();

                StringBuilder sb2 = new StringBuilder();
                int count = 0;
                for (int i = 0; i <= superDuper.length()-3; i+=3) {
                    String group1 = String.valueOf(superDuper.charAt(i));
                    String group2 = String.valueOf(superDuper.charAt(i+1));
                    String group3 = String.valueOf(superDuper.charAt(i+2));
                    int parity = (Integer.parseInt(group1) + Integer.parseInt(group2) + Integer.parseInt(group3)) % 2;
                    String encodedBytes = group1.repeat(2) + group2.repeat(2) + group3.repeat(2) + String.valueOf(parity).repeat(2);
                    sb2.append(encodedBytes);
                    count += 8;
                }

                String s = String.valueOf(superDuper.charAt(length - 2));
                if (length % 3 == 1) {
                    sb2.append(s.repeat(2)).append("0000").append(s.repeat(2));
                }

                if (length % 3 == 2) {
                    String t = String.valueOf(superDuper.charAt(length - 1));
                    sb2.append(s.repeat(2)).append(t.repeat(2)).append("00").append(String.valueOf(Integer.parseInt(s) + Integer.parseInt(t) % 2).repeat(2));
                }


                superDuper = sb2.toString();


                List<String> chopChop = new ArrayList<>();
                //String[] chopChop= new String[superDuper.length()/8 + 1];
                int i = 0;
                while (superDuper.length() > 0) {
                    if (superDuper.length() >= 8) {
                        chopChop.add(superDuper.substring(0, 8));
                        superDuper = superDuper.substring(8);
                        i++;
                    } else if (superDuper.length() <= 7) {
                        chopChop.add(superDuper);
                        break;
                    }

                }
                for (String st : chopChop) {
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
                    String a =String.valueOf(s.charAt(0));
                    String b =String.valueOf(s.charAt(1));
                    String c =String.valueOf(s.charAt(2));
                    String d =String.valueOf(s.charAt(3));
                    String e =String.valueOf(s.charAt(4));
                    String f =String.valueOf(s.charAt(5));
                    String g =String.valueOf(s.charAt(6));
                    String h =String.valueOf(s.charAt(7));



                    if (!g.equals(h)) {
                        sb.append(a).append(c).append(e);
                    }

                    if (!a.equals(b)) {
                        if ((Integer.parseInt(a) + Integer.parseInt(c) + Integer.parseInt(e)) % 2 == Integer.parseInt(g)) {
                            sb.append(a).append(c).append(e);
                        } else {
                            sb.append(b).append(c).append(e);
                        }
                    }

                    if (!c.equals(d)) {
                        if ((Integer.parseInt(a) + Integer.parseInt(c) + Integer.parseInt(e)) % 2 == Integer.parseInt(g)) {
                            sb.append(a).append(c).append(e);
                        } else {
                            sb.append(a).append(d).append(e);
                        }

                    }
                    if (!e.equals(f)) {
                        if ((Integer.parseInt(a) + Integer.parseInt(c) + Integer.parseInt(e)) % 2 == Integer.parseInt(g)) {
                            sb.append(a).append(c).append(e);
                        } else {
                            sb.append(a).append(d).append(f);
                        }

                    }
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
                    } else if (decodedBits.length() <= 7) {
                        decodedBytes.add(decodedBits);
                        break;
                    }
                }

                if (decodedBytes.get(decodedBytes.size() - 1).length() < 8) {
                    decodedBytes.remove(decodedBytes.size() - 1);
                }
//            String str = decodedBytes.get(decodedBytes.size() - 1);
//            Integer finalByte = Integer.valueOf(str, 2);
//            System.out.println(str);
//            int length = decodedBytes.size();
//            if (Integer.parseInt(str.substring(7)) == 0) {
//
//                String finalByteReal = str.substring(0, 7) + "1";
//                decodedBytes.set(length - 1, finalByteReal);
//            }



                //System.out.println();
                for (String s : decodedBytes) {
                    System.out.print(s + " ");
                    out.write((byte) (int)Integer.valueOf(s, 2));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

