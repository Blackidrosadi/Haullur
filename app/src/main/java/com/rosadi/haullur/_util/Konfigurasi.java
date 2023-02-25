package com.rosadi.haullur._util;

public class Konfigurasi {
    /**
     * URL FILE PHP
     */
    public static final String URL  = "http://192.168.1.34:8080/haullur/";
//    public static final String URL  = "https://fprg-haullur.000webhostapp.com/haullur/";

    public static final String URL_LOGIN                = URL + "akun/login.php";
    public static final String URL_LOAD_AKUN            = URL + "akun/load.php";
    public static final String URL_TAMBAH_AKUN          = URL + "akun/tambah.php";
    public static final String URL_TAMBAH_PENUGASAN     = URL + "akun/tambahPenugasan.php";
    public static final String URL_HAPUS_PENUGASAN      = URL + "akun/hapusPenugasan.php";

    public static final String URL_LOAD_KELUARGA            = URL + "keluarga/load.php";
    public static final String URL_LOAD_KELUARGA_BY_PETUGAS = URL + "keluarga/loadByPetugas.php?id=";
    public static final String URL_LOAD_KELUARGA_TERBARU    = URL + "keluarga/loadTerbaru.php";
    public static final String URL_CARI_KELUARGA            = URL + "keluarga/cari.php?cari=";

    public static final String URL_LOAD_KELUARGA_FREE       = URL + "keluarga/loadFree.php";
    public static final String URL_CARI_KELUARGA_FREE       = URL + "keluarga/cariFree.php?cari=";

    public static final String URL_TAMBAH_KELUARGA      = URL + "keluarga/tambah.php";
    public static final String URL_EDIT_KELUARGA        = URL + "keluarga/edit.php";
    public static final String URL_HAPUS_KELUARGA       = URL + "keluarga/hapus.php";

    public static final String URL_LOAD_ALMARHUM        = URL + "keluarga/loadAlmarhum.php?id=";
    public static final String URL_TAMBAH_ALMARHUM      = URL + "keluarga/tambahAlmarhum.php";
    public static final String URL_EDIT_ALMARHUM        = URL + "keluarga/editAlmarhum.php";
    public static final String URL_HAPUS_ALMARHUM       = URL + "keluarga/hapusAlmarhum.php";
    public static final String URL_LOAD_TOTAL_ALMARHUM  = URL + "keluarga/loadTotalAlmarhum.php?id=";

    public static final String URL_LOAD_PROGRAM_HAUL        = URL + "haul/loadProgram.php";
    public static final String URL_LOAD_PROGRAM_AKTIF_HAUL  = URL + "haul/loadProgramAktif.php";
    public static final String URL_TAMBAH_PROGRAM_HAUL      = URL + "haul/tambahProgram.php";
    public static final String URL_CEK_PROGRAM_HAUL         = URL + "haul/cekProgram.php";

    public static final String URL_LOAD_PENARIKAN           = URL + "penarikan/load.php?id=";
    public static final String URL_TRANSAKSI_PENARIKAN      = URL + "penarikan/tambah.php";
    /**
     * TAG / KEY
     */
    public static final String KEY_USER_PREFERENCE          = "user";
    public static final String KEY_USER_ID_PREFERENCE       = "id_pref";
    public static final String KEY_USER_NAMA_PREFERENCE     = "nama_pref";
    public static final String KEY_USER_EMAIL_PREFERENCE    = "email_pref";
    public static final String KEY_USER_TELEPON_PREFERENCE  = "telepon_pref";
    public static final String KEY_USER_SANDI_PREFERENCE    = "sandi_pref";
    public static final String KEY_USER_LEVEL_PREFERENCE    = "level_pref";
    public static final String KEY_JSON_ARRAY_RESULT        = "result";

    public static final String KEY_ID       = "id";
    public static final String KEY_NAMA     = "nama";
    public static final String KEY_EMAIL    = "email";
    public static final String KEY_TELEPON  = "telepon";
    public static final String KEY_SANDI    = "sandi";
    public static final String KEY_LEVEL    = "level";

    public static final String KEY_RT = "rt";

    public static final String KEY_ID_HAUL      = "id_haul";
    public static final String KEY_ID_KELUARGA  = "id_keluarga";
    public static final String KEY_ID_AKUN      = "id_akun";

    public static final String KEY_TANGGAL      = "tanggal";
    public static final String KEY_DESKRIPSI    = "deskripsi";
    public static final String KEY_STATUS       = "status";

    public static final String KEY_JUMLAH       = "jumlah";
}
