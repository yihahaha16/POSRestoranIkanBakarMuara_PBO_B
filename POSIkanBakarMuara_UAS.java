import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
interface CetakStruk {//interface untuk class yang mempunyai method sama namun logika cetak struk yang berbeda (PembayaranQRIS dan PembayaranCash)
    public void cetakStruk();
}

class Menu {//superclass
    enum Status {Tersedia, Habis}
    enum Kategori {Makanan, Minuman}
    static int counter = 1;
    String menuID;
    String menuNama;
    Status menuStatus;  
    Kategori menuKategori;        
    
    public Menu( String menuNama, Status menuStatus, Kategori menuKategori){//constructor
        this.menuID = "M"+ counter++;
        this.menuNama = menuNama;
        this.menuStatus = menuStatus;
        this.menuKategori = menuKategori;
    }

    public void tampilMenu(){
    }
}

class MenuHarga extends Menu {//inheritance karena ada dua jenis menu, satu pakai harga, satu pakai poin
    private int hargaMenuHarga;//encapsulation, dilindungi supaya tidak bisa diubah sembarangan oleh class lain
    public MenuHarga( String menuNama, Status menuStatus, int hargaMenuHarga, Kategori menuKategori){//constructor
        super(menuNama, menuStatus, menuKategori);//constructor dari superclass
        this.hargaMenuHarga = hargaMenuHarga;
    }
    public int getHargaMenuHarga(){ return hargaMenuHarga; }//getter untuk mengambil atribut yang private
    @Override
    public void tampilMenu(){
        if(menuStatus == Status.Habis){
            System.out.println(menuNama+" Rp"+getHargaMenuHarga()+" (HABIS)") ;
        }
        else{
            System.out.println(menuNama+" Rp"+getHargaMenuHarga()) ;
        }  
    }
}

class MenuPoin extends Menu {//inheritance
    private int hargaMenuPoin;//encapsulation
    public MenuPoin( String menuNama, Status menuStatus, int hargaMenuPoin, Kategori menuKategori){//constructor
        super( menuNama, menuStatus, menuKategori);//constructor dari superclass
        this.hargaMenuPoin = hargaMenuPoin;
    }
    public int getHargaMenuPoin(){ return hargaMenuPoin; }//getter
        @Override
    public void tampilMenu(){
        if(menuStatus == Status.Habis){
            System.out.println(menuNama+" "+getHargaMenuPoin()+" poin (HABIS)") ;
        }
        else{
            System.out.println(menuNama+" "+getHargaMenuPoin() +" poin");
        }   
    }
}

class Keranjang {//simpan pesanan sementara sebelum di bayar
    Menu menu;
    int kuantitas;
    public Keranjang(Menu menu, int kuantitas){
        this.menu = menu;
        this.kuantitas = kuantitas;
    }
}

class Pesanan {//superclass
    static int counter = 1;
    String pesananID;
    int pesananTotal;
    Pesanan(int pesananTotal) {
        this.pesananID = "P" + counter++;
        this.pesananTotal = pesananTotal;
    }
}

class DineIn extends Pesanan {
    String noMeja;
    DineIn(int pesananTotal, String noMeja) {//constructor
        super(pesananTotal);//constructor superclass
        this.noMeja = noMeja;
    }
}
class TakeAway extends Pesanan {
    TakeAway(int pesananTotal) {
        super(pesananTotal);//constructor superclass
    }
}

class Pelanggan  implements Serializable{//superclass
    enum Status { Member, Guest }
    private String nama; //encapsulation
    private Status pelangganStatus;
    public Pelanggan(String nama, Status pelangganStatus){ //constructor
        this.nama = nama;
        this.pelangganStatus = pelangganStatus;   
    }
    public String getNama(){ return nama; }//getter
    public Status getPelangganStatus(){ return pelangganStatus; }
}

class Member extends Pelanggan {//subclass
    private int poin = 0; //encapsulation
    private String pelangganNoHp;
    private String password; 
public Member(String pelangganNoHp, String password, String nama){
    super(nama, Pelanggan.Status.Member);//constructor superclass
    this.pelangganNoHp = pelangganNoHp;
    this.password = password;
}
    public String getPelangganNoHp(){ return pelangganNoHp; }//getter
    public int getPoin(){ return poin; }
    public String getPassword(){ return password; }
    public void setPoin(int poin){//setter
        this.poin+=poin;
    }
    public void hitungPoin(int totalBayar){//hitung total poin yang didapatkan
        int totalPoin = (totalBayar * 3) / 100;
        this.poin += totalPoin;
    }
    public void menampilkanPelanggan(){
        System.out.println("Nama: "+ getNama());
        System.out.println("No HP: " + getPelangganNoHp());
        System.out.println("Poin: " + poin);
    }
}

class Guest extends Pelanggan {//inheritance
    public Guest(String nama){//constructor
        super(nama, Pelanggan.Status.Guest);}//constructor superclass
}

abstract class Pembayaran {// abstraction yang disembunyikan adalah detail proses pembayaran seperti validasi saldo dan perhitungan kembalian. User hanya menggunakan method bayar() tanpa mengetahui implementasinya
    static float pajak = 0.1f;
    protected int total=0;//encapsulation protected biar bisa diakses sama childnya
    public Pembayaran(int total){
        this.total=total;
    }
    public int getTotal(){ return total; } //getter
    public abstract void bayar(); //abstraction
}

class PembayaranCash extends Pembayaran implements CetakStruk {//inheritance + interface
    private int saldo=0;
    public PembayaranCash(int total) { //constructor
        super(total);}

    public int getSaldo() {return saldo;}
    public void setSaldo(int saldo){
        this.saldo=saldo;}

    public int hitungKembalian(){ return saldo - getTotal(); }//method hitung kembalian
    public boolean cekPembayaran(int total, int saldo){//method logika pembayaran
        if(saldo < total){
            System.out.println("Saldo tidak mencukupi");
            return false;
        } else {
            return true;
        }}
    public boolean pembayaranBerhasil(){
    return saldo >= getTotal();}

    @Override//polymorphism
    public void bayar(){
        System.out.println("Uang yang diterima Rp" + saldo);
        System.out.println();
        if(!cekPembayaran(getTotal(), saldo)){
            System.out.println("Uang anda kurang Rp" + (getTotal() - saldo));
            return;
        }}
    @Override//polymorphism
    public void cetakStruk(){
        System.out.println("Total: Rp" + getTotal());
        System.out.println("Bayar: Rp" + saldo);
        System.out.println("Kembalian: Rp" + hitungKembalian());
    }
}

class PembayaranQRIS extends Pembayaran implements CetakStruk {//inheritance + interface
    public PembayaranQRIS(int total){
        super(total);
    }
    @Override//polymorphism
    public void bayar(){
        System.out.println("====== Silahkan scan QRIS ======");
        System.out.println("Total pesanan anda Rp" + getTotal());
        System.out.println("****SISTEM****\nSedang menghubungkan ke akun anda\nMendapatkan total yang harus dibayar\nCek saldo apakah mencukupi\nSaldo anda mencukupi\nPembayaran berhasil\nSaldo anda berkurang sebesar "+ getTotal());}
    @Override//polymorphism
    public void cetakStruk(){
        System.out.println("Total: Rp" + getTotal());
    }
}

public class POSIkanBakarMuara_UAS {
    static Scanner input = new Scanner(System.in);//scanner untuk input
    static ArrayList<Member> members = new ArrayList<>();//array list karena fleksibel, tidak tau berapa banyak yang akan diinput (menampung member)
    static boolean isMember=false; //sebagai penanda kalau login sebagai member
    static MenuHarga[] menuHarga = new MenuHarga[10]; //menu yang bayar pakai uang
    static MenuPoin[] menuPoin = new MenuPoin[4]; //menu  yang bayar pakai poin
    static ArrayList<Keranjang> keranjangPelanggan = new ArrayList<>(); //keranjang untuk menu harga
    static ArrayList<Keranjang> keranjangPoin= new ArrayList<>(); //keranjang untuk menu poin
    static Guest guestSekarang; //terisi kalau login sbg guest
    static int usedMember=0; //terisi kalau login sbg member, simpan indeksnya array list members 
    static boolean dariKeranjang=false; //penanda untuk kembali ke halaman keranjang
    static Pesanan pesanan;
    static int subtotal=0, pajak=0, totalAkhir=0, poin=0;
    static int jumlahMenu(){ //hitung menu otomatis untuk opsi bayar, lihat keranjang dll akan menyesuaikan total menu yang ada
    int total = menuHarga.length;
    if(isMember){
        total += menuPoin.length;
    }
    return total;}

    static void simpanMember() {//simpan member ke txt https://www.geeksforgeeks.org/java/serialization-and-deserialization-in-java/
    try {
        ObjectOutputStream out =new ObjectOutputStream(new FileOutputStream("member.txt"));
        out.writeObject(members);
        out.close();}
    catch(Exception e) {
        System.out.println("Gagal menyimpan member");
    }}

    static void loadMember() {//baca member txt
    try {
        ObjectInputStream in =new ObjectInputStream(new FileInputStream("member.txt"));
        members = (ArrayList<Member>) in.readObject();
        in.close();
    }
    catch(Exception e) {
        members = new ArrayList<>();
    }}

    //dashboard sebelum login
    static void dashboardSebelumLogin() {
        System.out.println("\n=== Selamat Datang di Ikan Bakar Muara ===\n1. Login Member\n2. Daftar menjadi Member\n3. Lanjut Sebagai Guest\n0. Keluar");
        System.out.print("Pilih: "); String pilih = input.nextLine();//pilih opsi, ada double input.nextLine() untuk mengecualikan enter
        switch(pilih) {
            case "1": loginMember(); break;
            case "2": daftarMember(); break;
            case "3": masukGuest(); break;
            case "0": keluar(); break;
            default:
                System.out.println("Pilihan tidak valid");
                dashboardSebelumLogin();
        }}
    
    //login member
    static void loginMember() {
        System.out.println("\n=== Login Member ===");
        System.out.print("Nomor Telepon: "); String noHp = input.nextLine();
        System.out.print("Password     : "); String password = input.nextLine();
        try{//validasi angka & cek apakah member & password tersedia https://www.geeksforgeeks.org/java/check-if-a-given-string-is-a-valid-number-integer-or-floating-point-in-java/
            Long.parseLong(noHp);
            Member ditemukan = null;//variabel dengan tipe data class menampung 1 buah nohp&pass
            for (int j=0;j<members.size();j++) {
            if (members.get(j).getPelangganNoHp().equals(noHp) && members.get(j).getPassword().equals(password)) {
                ditemukan = members.get(j);
                usedMember=j;
                break;}}
            if (ditemukan == null) {
                System.out.println("Maaf, nomor telepon atau password salah!\n1. Login ulang\n0. Kembali");
               while (true) {
                    System.out.print("Pilih: ");String pilih = input.nextLine();
                    if (pilih.equals("1")) {
                        loginMember();break;} 
                    else if (pilih.equals("0")) {
                        dashboardSebelumLogin();break;} 
                    else {
                        System.out.println("Pilihan tidak valid");}}
            } else {
                System.out.println("Login Berhasil!");
                isMember=true;
                dashboardSetelahLogin();
            }}
        catch (NumberFormatException e){
            System.out.println("Maaf, nomor telepon harus berupa angka!\n1. Login ulang\n0. Kembali");
               while (true) {
                    System.out.print("Pilih: ");String pilih = input.nextLine();
                    if (pilih.equals("1")) {
                        loginMember();break;} 
                    else if (pilih.equals("0")) {
                        dashboardSebelumLogin();break;} 
                    else {
                        System.out.println("Pilihan tidak valid");}}}}

    //daftar member    
    static void daftarMember() {
        System.out.println("\n=== Daftar Menjadi Member ===");
        System.out.print("Nama Lengkap  : "); String nama = input.nextLine();
        System.out.print("Nomor Telepon : "); String noHp = input.nextLine();
        System.out.print("Password      : ");  String password = input.nextLine();
        try{//validasi angka & cek apakah member & password tersedia https://www.geeksforgeeks.org/java/check-if-a-given-string-is-a-valid-number-integer-or-floating-point-in-java/
            Long.parseLong(noHp);
            for (Member m : members) {
            if (m.getPelangganNoHp().equals(noHp)) {
                System.out.println("Maaf, nomor telepon telah terdaftar!\n1. Login Member\n2. Daftar ulang\n0. Kembali");
               while (true) {
                    System.out.print("Pilih: ");String pilih = input.nextLine();
                    if (pilih.equals("1")) {
                        loginMember();break;}
                    else if(pilih.equals("2")){
                        daftarMember();break;
                    } 
                    else if (pilih.equals("0")) {
                        dashboardSebelumLogin();break;} 
                    else {
                        System.out.println("Pilihan tidak valid");}}}}
            members.add(new Member(noHp, password, nama));
            simpanMember();//simpan member yang didaftar
        System.out.println("Pendaftaran Member BERHASIL!\n1. Login Member\n0. Kembali");
         while (true) {
                    System.out.print("Pilih: ");String pilih = input.nextLine();
                    if (pilih.equals("1")) {
                        loginMember();break;} 
                    else if (pilih.equals("0")) {
                        dashboardSebelumLogin();break;} 
                    else {
                        System.out.println("Pilihan tidak valid");}}}
        catch (NumberFormatException e){
            System.out.println("Maaf, nomor telepon harus berupa angka!\n1. Daftar ulang\n0. Kembali");
               while (true) {
                    System.out.print("Pilih: ");String pilih = input.nextLine();
                    if (pilih.equals("1")) {
                        daftarMember();break;} 
                    else if (pilih.equals("0")) {
                        dashboardSebelumLogin();break;} 
                    else {
                        System.out.println("Pilihan tidak valid");}}}}
    
    //masuk sebagai guest
    static void masukGuest() {
        System.out.print("\n=== Login sebagai guest ===\nNama: ");
        String nama=input.nextLine(); 
        guestSekarang = new Guest(nama);
        dashboardSetelahLogin();
    }

    //dashboard setelah login utk guest dan member
    static void dashboardSetelahLogin() {
        System.out.println("\n=== Dashboard ===\n1. Lihat Menu\n2. Pesan\n3. Lihat Keranjang");
        if(isMember){
            System.out.println("4. Lihat Informasi Member");}
        System.out.println("0. Logout");
System.out.print("Pilih: "); String pilih = input.nextLine();
        switch(pilih) {
            case "1": 
            System.out.println("\n=== Menu ===");
            lihatMenu();
            System.out.println(jumlahMenu() + 1 + ". Pesan");
            System.out.println("0. Kembali");
            lihatMenu1(); 
            break;

            case "2":
            System.out.println("\n=== Pesan ===");    
            lihatMenu();
            System.out.println(jumlahMenu()+1+". Lihat Keranjang");
            System.out.println(jumlahMenu()+2+". Bayar");
            System.out.println("0. Kembali");
            pesan();
            break;

            case "3": lihatKeranjang(); break;
            case "4": lihatInfoMember();break;
            case "0": logout(); break;
            default:
                System.out.println("Pilihan tidak valid");    
                dashboardSetelahLogin();  
        }}
    
    //untuk menampilkan menu
    static void lihatMenu(){
        int count=1;
        System.out.println("=== Makanan ===");
        for(MenuHarga menu: menuHarga){
            if(menu != null &&(menu.menuKategori.equals(Menu.Kategori.Makanan))){
                System.out.print(count+". ");
                menu.tampilMenu();
                count++;}}
        System.out.println("=== Minuman ===");
        for(MenuHarga menu: menuHarga){
            if(menu != null &&(menu.menuKategori.equals(Menu.Kategori.Minuman))){
                System.out.print(count+". ");
                count++;
                menu.tampilMenu();}}
        if(isMember){
        System.out.println("=== Poin ===");
        for(MenuPoin menu: menuPoin){
            System.out.print(count+". ");
            count++;    
            menu.tampilMenu();}}
    }
   
    //untuk input di halaman menu (agar lihat menu bisa digunakan banyak kali)
    static void lihatMenu1(){
        System.out.print("Pilih: "); String pilih = input.nextLine();
        if(pilih.equals("0")){
            dashboardSetelahLogin();
        }
        else if(pilih.equals(String.valueOf(jumlahMenu() + 1))){
            System.out.println("\n=== Pesan ===");
            lihatMenu();
            System.out.println(jumlahMenu() + 1+". Lihat Keranjang");
            System.out.println(jumlahMenu() + 2+". Bayar");
            System.out.println("0. Kembali");
            pesan();
        }
        else{
            System.out.println("Pilih menu Pesan untuk mulai memesan");
            lihatMenu1();
        }}

    static void pesan(){
    while (true) {
        System.out.print("Pilih: "); String pilih = input.nextLine();
        int pilihInt;
        try {
            pilihInt = Integer.parseInt(pilih);
        } catch (NumberFormatException e) {
            System.out.println("Pilihan tidak valid");
            continue;
        }
        if(pilih.equals("0")){
            if(dariKeranjang){
                dariKeranjang=false;
                lihatKeranjang();}
            else{
                dashboardSetelahLogin();}}
        else if(pilihInt>=1&&pilihInt<=10){
            if(menuHarga[pilihInt-1].menuStatus.equals(Menu.Status.Habis)){
                System.out.println("Maaf menu habis");
                pesan();}
            else{
 int kuantitasInt;
          while (true) {
    System.out.print("Kuantitas: ");
    try {
        kuantitasInt = Integer.parseInt(input.nextLine());
        break;
    } catch (NumberFormatException e) {
        System.out.println("Input tidak valid");
    }
}
               Menu menuHargaDipilih = menuHarga[pilihInt - 1];
                boolean ditemukan = false;
                for (Keranjang k : keranjangPelanggan) {
                    if (k.menu == menuHargaDipilih) {
                        k.kuantitas += kuantitasInt;
                        ditemukan = true;
                        break;}}
                if (!ditemukan) {
                    keranjangPelanggan.add(new Keranjang(menuHargaDipilih, kuantitasInt));}
                System.out.println("Menu "+menuHarga[pilihInt-1].menuNama+" ("+kuantitasInt+") berhasil ditambahkan ke keranjang");
                pesan();}}
        else{
            if(isMember&&pilihInt>=11&&pilihInt<=14){
                if(menuPoin[pilihInt-menuPoin.length-1].menuStatus.equals(Menu.Status.Habis)){
                    System.out.println("Maaf menu habis");}
                else if(members.get(usedMember).getPoin()<menuPoin[pilihInt-menuPoin.length-1].getHargaMenuPoin()){
                    System.out.println("Maaf poin anda tidak mencukupi");}
                else{
                 int kuantitasInt;
                    while (true) {
                System.out.print("Kuantitas: ");
                try {
                    kuantitasInt = Integer.parseInt(input.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid");
                }
            }
                    if(members.get(usedMember).getPoin()>=menuPoin[pilihInt-menuPoin.length-1].getHargaMenuPoin()*kuantitasInt){
                    Menu menuPoinDipilih = menuPoin[pilihInt - menuPoin.length - 1];
                    boolean ditemukan = false;
                    for (Keranjang k : keranjangPoin) {
                        if (k.menu == menuPoinDipilih) {
                            k.kuantitas += kuantitasInt;
                            ditemukan = true;
                            break;}}
                    if (!ditemukan) {
                        keranjangPoin.add(new Keranjang(menuPoinDipilih, kuantitasInt));}
                    System.out.println("Menu "+menuPoin[pilihInt-menuPoin.length-1].menuNama+" ("+kuantitasInt+") berhasil ditambahkan ke keranjang");}   
                    else{
                        System.out.println("Maaf poin anda tidak mencukupi");}
                    pesan();}}
            else if(pilih.equals(String.valueOf(jumlahMenu() + 1))){
                lihatKeranjang();}
            else if(pilih.equals(String.valueOf(jumlahMenu() + 2))){
                bayar();}
            else{
                System.out.println("Pilihan tidak valid");
                pesan();}}}}

    //lihat isi keranjang/pesanan yang udah dipesan
    static void lihatKeranjang(){
        System.out.println("\n=== Keranjang ===");
        if (keranjangPelanggan.isEmpty()) {
            System.out.println("Keranjang kosong");}
        for(int i=0;i<keranjangPelanggan.size();i++){
            System.out.println(keranjangPelanggan.get(i).menu.menuNama+" ("+keranjangPelanggan.get(i).kuantitas+")");} //arraylist memang pakai get(i) gabisa [i]
        for(int j=0;j<keranjangPoin.size();j++){
            System.out.println(keranjangPoin.get(j).menu.menuNama+" ("+keranjangPoin.get(j).kuantitas+")");} //arraylist memang pakai get(i) gabisa [i]
        System.out.println("1. Tambah Pesanan\n2. Bayar\n3. Hapus item\n0. Kembali");
        System.out.print("Pilih: "); String pilih = input.nextLine(); //pilih opsi, ada double input.nextLine() untuk mengecualikan enter
        switch (pilih) {
            case "1":
                System.out.println("\n=== Pesan ===");
                lihatMenu();
                System.out.println("0. Kembali");
                dariKeranjang = true;
                pesan();break;
            case "2":
                if (keranjangPelanggan.isEmpty() && keranjangPoin.isEmpty()) {
                    System.out.println("Keranjang kosong!");
                    lihatKeranjang();}
                bayar();break;
            case "3":
                hapus(); break;
            case "0":
                dashboardSetelahLogin();break;
            default:
                System.out.println("Pilihan tidak valid");
                lihatKeranjang();break;}}

    //lihat poin member
    static void lihatInfoMember(){
        System.out.println("\n=== Informasi Member ===");
        members.get(usedMember).menampilkanPelanggan();
        System.out.print("0. Kembali\nPilih: ");String pilih = input.nextLine();
    if(pilih.equals("0")){
        dashboardSetelahLogin();}
    else{
        System.out.println("Pilihan tidak valid");
        lihatInfoMember();}}

    //menangani pembayaran
    static void bayar(){
        subtotal = 0; poin=0;
        System.out.println("\n=== Pesanan ===");
        for(int i=0;i<keranjangPelanggan.size();i++){
            MenuHarga mh = (MenuHarga) keranjangPelanggan.get(i).menu;//casting dari tipe keranjang ke tipe menu harga biar bisa pakai getnya
            System.out.println(keranjangPelanggan.get(i).menu.menuNama+" ("+keranjangPelanggan.get(i).kuantitas+") = Rp"+(mh.getHargaMenuHarga()*keranjangPelanggan.get(i).kuantitas));
            subtotal+=mh.getHargaMenuHarga()*keranjangPelanggan.get(i).kuantitas;}
        if(isMember){
            if(keranjangPoin.size()>0){
                System.out.println("\n=== Pesanan dengan Poin ===");}
                for(int j=0;j<keranjangPoin.size();j++){
                    MenuPoin mp = (MenuPoin) keranjangPoin.get(j).menu;
                    System.out.println((keranjangPelanggan.size()+j+1)+". "+keranjangPoin.get(j).menu.menuNama+" ("+keranjangPoin.get(j).kuantitas+") = "+(mp.getHargaMenuPoin()*keranjangPoin.get(j).kuantitas)+" Poin");
                    poin+=mp.getHargaMenuPoin()*keranjangPoin.get(j).kuantitas;}}
        pajak = (int)(subtotal * Pembayaran.pajak);
        totalAkhir = subtotal + pajak;
        System.out.println("\n====================");
        System.out.println("Subtotal: Rp" + subtotal +"\nPajak (10%): Rp" + pajak+ "\nTotal Bayar: Rp" + totalAkhir);
        if(isMember) System.out.println("\nPoin Terpakai: "+poin+" poin");                
        System.out.println("====================");
        System.out.println("\n=== Tipe Pesanan ===\n1. Dine In\n2. Take Away");
        System.out.print("Pilih: ");String tipe = input.nextLine();
        if(tipe.equals("1")){
            System.out.print("Nomor Meja: ");
            String noMeja = input.nextLine();
            pesanan = new DineIn(totalAkhir, noMeja);}
        else if(tipe.equals("2")){
            pesanan = new TakeAway(totalAkhir);}
        else{
            System.out.println("Pilihan tidak valid");
            bayar();
            return;}
        System.out.println((1)+". Bayar Cash\n"+(2)+". Bayar QRIS\n0. Kembali");
        System.out.print("Pilih: "); String pilih = input.nextLine();
        if(pilih.equals("0")){
            dashboardSetelahLogin();}
        else if(pilih.equals("1")){
            PembayaranCash cash = new PembayaranCash(totalAkhir);
            System.out.println("\n=== Pembayaran Cash ===");
            System.out.println("Silahkan bayar di kasir dengan nominal Rp" + totalAkhir);
            System.out.print("Masukkan uang: ");
            int saldo = input.nextInt();input.nextLine();
            if (saldo < 0) {
                System.out.println("Uang tidak valid");return;}
            cash.setSaldo(saldo);
            cash.bayar();
        if (cash.pembayaranBerhasil()) {
            cetakUIStruk();
            cash.cetakStruk();
        }
        else if(!cash.pembayaranBerhasil()){
            bayar();
            return;}}
        else if(pilih.equals("2")){
            System.out.println("\n=== Pembayaran QRIS ===");
            PembayaranQRIS qris = new PembayaranQRIS(totalAkhir);
            qris.bayar();
            cetakUIStruk();
            qris.cetakStruk();}
        else{
            System.out.println("Pilihan tidak valid");
            bayar();}
        if(isMember){
            System.out.println("Total poin digunakan: "+ poin+" poin");
            members.get(usedMember).setPoin(-1*poin);
            System.out.println("Sisa Poin: "+ members.get(usedMember).getPoin()+" poin");
            members.get(usedMember).hitungPoin(totalAkhir);
            simpanMember();
            System.out.println("\n===Penambahan Poin===\nPenambahan Poin: "+  ((totalAkhir * 3) / 100) +" poin");
            System.out.println("Total Poin: "+ members.get(usedMember).getPoin()+" poin");}
        System.out.println("\n===Terima Kasih===");
        keranjangPelanggan.clear();keranjangPoin.clear(); //hapus keranjang
        System.out.println("0. Kembali");
        selesaiBayar();}

    //hapus pesanan dari keranjang
    static void hapus(){
    String pilih; int pilihInt;
    System.out.println("\n=== Hapus ===");
    if (keranjangPelanggan.isEmpty() && keranjangPoin.isEmpty()) {
        System.out.println("Keranjang kosong\nPilih: ");pilih = input.nextLine();
        if (pilih.equals("0")) {
            dashboardSetelahLogin();}return;}
    for (int i = 0; i < keranjangPelanggan.size(); i++) {
        System.out.println((i + 1) + ". " + keranjangPelanggan.get(i).menu.menuNama +" (" + keranjangPelanggan.get(i).kuantitas + ")");}
    System.out.println("0. Kembali");
    while (true) {
        System.out.print("Pilih pesanan yang ingin dihapus: ");pilih = input.nextLine();
        try {
            pilihInt = Integer.parseInt(pilih);} 
        catch (NumberFormatException e) {
            System.out.println("Pilihan tidak valid");continue;}
        if (pilihInt == 0) {
            dashboardSetelahLogin();return;}
        else if (pilihInt >= 1 && pilihInt <= keranjangPelanggan.size()) {
            System.out.println(keranjangPelanggan.get(pilihInt - 1).menu.menuNama + " berhasil dihapus");
            keranjangPelanggan.remove(pilihInt - 1);return;}
        else if (isMember && pilihInt > keranjangPelanggan.size()&& pilihInt <= keranjangPelanggan.size() + keranjangPoin.size()) {
            System.out.println(keranjangPoin.get(pilihInt - keranjangPelanggan.size() - 1).menu.menuNama+ " berhasil dihapus");
            keranjangPoin.remove(pilihInt - keranjangPelanggan.size() - 1);return;}
        else {
            System.out.println("Pilihan tidak valid");}}}

    //untuk mencetak rincian pesanan di struk    
    static void rincianPesanan(){
        for(int i=0;i<keranjangPelanggan.size();i++){
            MenuHarga mh = (MenuHarga) keranjangPelanggan.get(i).menu;//casting dari tipe keranjang ke tipe menu harga biar bisa pakai getnya
            System.out.println(keranjangPelanggan.get(i).menu.menuNama+" ("+keranjangPelanggan.get(i).kuantitas+") = Rp"+(mh.getHargaMenuHarga()*keranjangPelanggan.get(i).kuantitas));}
        if(isMember){
            if(keranjangPoin.size()>0){
                System.out.println("\n=== Pesanan dengan Poin ===");}
                for(int j=0;j<keranjangPoin.size();j++){
                    MenuPoin mp = (MenuPoin) keranjangPoin.get(j).menu;
                    System.out.println((keranjangPelanggan.size()+j+1)+". "+keranjangPoin.get(j).menu.menuNama+" ("+keranjangPoin.get(j).kuantitas+") = "+(mp.getHargaMenuPoin()*keranjangPoin.get(j).kuantitas)+" Poin");}}
                System.out.println("\n===============");
                System.out.println("Subtotal: Rp" + subtotal +"\nPajak (10%): Rp" + pajak);
                if(isMember) System.out.println("\nPoin Terpakai: "+poin+" poin");                
                }

    static void cetakUIStruk(){
        System.out.println("\n======= STRUK PEMBAYARAN =======");
        System.out.println("================================");
        System.out.println("ID Pesanan: " + pesanan.pesananID);
        if(!isMember){
            System.out.println("Nama: "+guestSekarang.getNama());}
        else{
            members.get(usedMember).menampilkanPelanggan();}
        if(pesanan instanceof DineIn){
            System.out.println("Tipe: Dine In");
            System.out.println("No Meja: " + ((DineIn) pesanan).noMeja);}
        else {
            System.out.println("Tipe: Take Away");}
        System.out.println("\n=== Pesanan ===");
        rincianPesanan();
        }
        
            
   //logout         
    static void logout() {
        isMember = false;//hapus penanda member
        usedMember = -1;//hapus penanda member
        guestSekarang = null; //kosongin guest
        dashboardSebelumLogin();}

    //quit
    static void keluar(){
        System.out.println("=== Terima Kasih ===");System.exit(0);}

    //untuk loop input di method bayar
    static void selesaiBayar(){    
        System.out.print("Pilih: ");
        String pilih = input.nextLine();
        if(pilih.equals("0")){
            dashboardSetelahLogin();}
        else{
            System.out.println("Pilihan tidak valid");
            selesaiBayar();}}
    public static void main(String[] args) {
        menuHarga[0]= new MenuHarga( "Udang Wangkang Bakar Madu", Menu.Status.Tersedia, 45000, Menu.Kategori.Makanan);
        menuHarga[1]= new MenuHarga( "Ayam Bakar Rica", Menu.Status.Tersedia, 25220, Menu.Kategori.Makanan);
        menuHarga[2]= new MenuHarga("Pakcoy Tahu", Menu.Status.Habis, 18000, Menu.Kategori.Makanan);
        menuHarga[3]= new MenuHarga( "Nila Bakar Muara", Menu.Status.Tersedia, 26000, Menu.Kategori.Makanan);
        menuHarga[4]= new MenuHarga( "Nasi Putih", Menu.Status.Tersedia, 7000, Menu.Kategori.Makanan);
        menuHarga[5]= new MenuHarga( "Kol Goreng", Menu.Status.Tersedia, 7000, Menu.Kategori.Makanan);
        menuHarga[6]= new MenuHarga( "Tahu Goreng", Menu.Status.Tersedia, 10000, Menu.Kategori.Makanan);
        menuHarga[7]= new MenuHarga( "Lemon Tea", Menu.Status.Tersedia, 7000, Menu.Kategori.Minuman);
        menuHarga[8]= new MenuHarga( "Milo", Menu.Status.Tersedia, 8000, Menu.Kategori.Minuman);
        menuHarga[9]= new MenuHarga("Es Timun Selasih", Menu.Status.Tersedia, 12000, Menu.Kategori.Minuman);
        menuPoin[0]= new MenuPoin( "Es Timun Selasih", Menu.Status.Tersedia, 12000, Menu.Kategori.Minuman);
        menuPoin[1]= new MenuPoin( "Udang Wangkang Bakar Madu", Menu.Status.Tersedia, 45000, Menu.Kategori.Makanan);
        menuPoin[2]= new MenuPoin( "Pakcoy Tahu", Menu.Status.Habis, 18000, Menu.Kategori.Makanan);
        menuPoin[3]= new MenuPoin( "Nila Bakar Muara", Menu.Status.Tersedia, 26000, Menu.Kategori.Makanan);
        loadMember();
        dashboardSebelumLogin();}
} 
