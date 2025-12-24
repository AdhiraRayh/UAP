A. CARA MENJALANKAN PROGRAM
1. Sistem Requirements
   Java Version: Java 8 atau lebih tinggi (disarankan Java 11+)

Memory: Minimal 512MB RAM

Storage: 10MB ruang kosong

Operating System: Windows, macOS, atau Linux

2. Langkah-langkah Menjalankan Program
   Via IDE (Eclipse/IntelliJ/NetBeans)
    1. Buka proyek di IDE
    2. Pastikan semua file dalam struktur package yang benar:
   com.kuliah.pemlan.main
   com.kuliah.pemlan.gui
   com.kuliah.pemlan.model
   com.kuliah.pemlan.service
   com.kuliah.pemlan.util
   3. Run file: Main.java
   4. Aplikasi akan terbuka secara otomatis
3. Struktur File Penting
   📁 LifeInsightManager+
   ├── 📁 com/kuliah/pemlan/
   │   ├── 📁 main/
   │   │   └── Main.java          # Entry point aplikasi
   │   ├── 📁 gui/
   │   │   ├── MainFrame.java     # Window utama
   │   │   ├── DashboardPanel.java
   │   │   ├── DataPanel.java
   │   │   ├── InputPanel.java
   │   │   └── ReportPanel.java
   │   ├── 📁 model/
   │   │   ├── Transaction.java
   │   │   └── TransactionList.java
   │   ├── 📁 service/
   │   │   ├── AnalysisService.java
   │   │   ├── FileManager.java
   │   │   └── ValidationService.java
   │   └── 📁 util/
   │       ├── IDGenerator.java
   │       └── ChartHelper.java
   ├── 📄 transactions.csv         # File data (auto-generated)
   └── 📄 README.txt              # Dokumentasi


B. TAMPILAN UTAMA & NAVIGASI
1. Tampilan Awal (Dashboard)
   ┌─────────────────────────────────────────┐
   │  [Dashboard] [Data] [Input] [Report]    │ ← Navigation Bar
   ├─────────────────────────────────────────┤
   │  Life Insight Manager+ Dashboard        │
   │  Analisis Perilaku & Pengeluaran Emosional│
   ├─────────────────────────────────────────┤
   │  ┌─────┐ ┌─────┐ ┌─────┐ ┌─────┐       │
   │  │Total│ │Rata2│ │Jml  │ │Emosi│       │ ← Stat Cards
   │  │Rp0  │ │Rp0  │ │0    │ │Rp0  │       │
   │  └─────┘ └─────┘ └─────┘ └─────┘       │
   │                                         │
   │  ┌─────────────────────────────────┐   │
   │  │       Aksi Cepat                │   │ ← Quick Actions
   │  │  • Tambah Transaksi             │   │
   │  │  • Lihat Data                   │   │
   │  │  • Lihat Laporan                │   │
   │  │  • Analisis                     │   │
   │  └─────────────────────────────────┘   │
   │                                         │
   │  ┌─────────────────────────────────┐   │
   │  │    Transaksi Terakhir           │   │ ← Recent Transactions
   │  │  Tanggal | Deskripsi | Jumlah   │   │
   │  │  ...                            │   │
   │  └─────────────────────────────────┘   │
   └─────────────────────────────────────────┘
2. Navigasi Antar Halaman 
Kembali ke halaman utama = Ctrl+D
Lihat semua transaksi dalam tabel = Ctrl+t
Tambah transaksi baru = Ctrl+I
Lihat analisis dan laporan = Ctrl+R

C. FITUR-FITUR UTAMA

FITUR 1: DASHBOARD (HALAMAN UTAMA)
1.1 Statistik Ringkasan
┌─────────────────────────────┐
│ Total Pengeluaran: Rp 0     │
│ Rata-rata: Rp 0             │
│ Jumlah Transaksi: 0         │
│ Pengeluaran Emosional: Rp 0 │
└─────────────────────────────┘
Total Pengeluaran: Jumlah semua transaksi
Rata-rata: Rata-rata per transaksi
Jumlah Transaksi: Total transaksi yang tersimpan
Pengeluaran Emosional: Total pengeluaran dengan emosi non-netral > Rp50.000

1.2 Aksi Cepat
Tambah Transaksi: Langsung ke halaman input
Lihat Data: Langsung ke halaman tabel data
Lihat Laporan: Langsung ke halaman analisis
Analisis: Sama seperti laporan

1.3 Transaksi Terakhir
Menampilkan 5 transaksi terakhir
Format: Tanggal, Deskripsi, Jumlah, Emosi
Auto-refresh saat ada perubahan data

FITUR 2: INPUT TRANSAKSI
2.1 Form Input Data
FORM INPUT TRANSAKSI
────────────────────
ID: (Auto-generate)              → ID otomatis (TRX2512240208001)
Tanggal (YYYY-MM-DD): 2025-12-24 → Format tanggal
Deskripsi: [__________________]  → Contoh: Kopi Starbucks
Jumlah (Rp): [________]          → Contoh: 50000
Kategori: [Pilih kategori...] ▼  → Dropdown pilihan
Kondisi Emosi: [Pilih emosi...] ▼→ Dropdown emosi
Catatan: [__________________]    → Opsional

2.2 Validasi Input
// Contoh validasi yang dilakukan:
1. Tanggal: Format YYYY-MM-DD, tidak boleh masa depan
2. Deskripsi: Tidak kosong, max 100 karakter
3. Jumlah: Harus angka > 0
4. Kategori: Harus dipilih (bukan default)
5. Emosi: Harus dipilih (bukan default)

2.3 Tombol Aksi
simpan = hijau
reset = biru
batal = merah

2.4 Mode Edit Transaksi
// Cara mengedit:
1. Buka halaman Data
2. Pilih transaksi di tabel
3. Klik tombol "Edit"
4. Form akan terisi data lama
5. Ubah data → klik Simpan


FITUR 3: DATA TRANSAKSI (TABEL)
3.1 Tampilan Tabel
┌──┬────────────┬────────────────┬──────────┬──────────┬──────┬────────┐
│ID│ Tanggal    │ Deskripsi      │ Jumlah   │ Kategori │Emosi │ Catatan│
├──┼────────────┼────────────────┼──────────┼──────────┼──────┼────────┤
│  │2025-12-24  │Kopi Starbucks  │Rp50,000  │Makanan   │Senang│Meeting │
│  │2025-12-24  │Bensin          │Rp100,000 │Transport │Netral│        │
└──┴────────────┴────────────────┴──────────┴──────────┴──────┴────────┘

3.2 Konfirmasi Hapus
┌─────────────────────────┐
│ Konfirmasi Hapus        │
├─────────────────────────┤
│ Apakah Anda yakin ingin │
│ menghapus transaksi ini?│
│                         │
│      [Ya]    [Tidak]    │
└─────────────────────────┘


FITUR 4: LAPORAN & ANALISIS
4.1 Tab "Ringkasan"
Statistik dasar (sama seperti dashboard)
Display dalam box berwarna

4.2 Tab "Per Kategori"
PENGELUARAN PER KATEGORI
────────────────────────
[PIE CHART VISUAL]
Makanan & Minuman: ████████ 40%
Transportasi:       ██████   30%
Hiburan:            ████     20%
Belanja:            ██       10%

4.3 Tab "Analisis Emosi"
ANALISIS PENGELUARAN BERDASARKAN EMOSI
──────────────────────────────────────
Senang:  Rp 500,000 (50%)
Stres:   Rp 300,000 (30%)
Netral:  Rp 200,000 (20%)

INSIGHT:
- Anda banyak spending saat stres (>30%)
- Coba teknik relaksasi sebelum belanja

4.4 Tab "Insight"
ANALISIS PENGELUARAN EMOSIONAL
──────────────────────────────
Total Transaksi: 15
Total Pengeluaran: Rp 2,500,000
Rata-rata per Transaksi: Rp 166,667

PENGELUARAN BERDASARKAN EMOSI:
Senang: Rp 1,250,000 (50.0%)
Stres:  Rp 750,000 (30.0%)
Netral: Rp 500,000 (20.0%)

Pengeluaran Impulsif (> Rp100.000): Rp 1,000,000 (40.0%)

4.5 Rekomendasi (Auto-generated)
💡 REKOMENDASI:
1. Buat anggaran bulanan untuk kategori pengeluaran tertinggi
2. Tunggu 24 jam sebelum belanja besar saat emosi tidak stabil
3. Review pengeluaran mingguan untuk identifikasi pola
4. Alokasikan 20% pendapatan untuk tabungan dan investasi
5. Catat setiap pengeluaran segera setelah terjadi


D. CONTOH KASUS PENGGUNAAN
Kasus 1: Mencatat Pengeluaran Harian
1. Klik tombol "Input" di navbar
2. Isi form:
    - Tanggal: 2025-12-24
    - Deskripsi: Kopi Starbucks
    - Jumlah: 50000
    - Kategori: Makanan & Minuman
    - Emosi: Senang
    - Catatan: Meeting pagi dengan klien
3. Klik "Simpan"
4. ID akan digenerate: TRX2512240930001
5. Data otomatis muncul di Dashboard dan Tabel

Kasus 2: Analisis Pola Pengeluaran
1. Setelah ada 10+ transaksi
2. Buka halaman "Report"
3. Lihat tab "Per Kategori" → tahu kategori dominan
4. Lihat tab "Analisis Emosi" → tahu hubungan emosi-pengeluaran
5. Lihat tab "Insight" → dapatkan rekomendasi

Kasus 3: Edit Transaksi yang Salah
1. Buka halaman "Data"
2. Cari transaksi di tabel (bisa pakai filter)
3. Klik baris yang mau diedit
4. Klik tombol "Edit"
5. Ubah data di form (misal: jumlah dari 50000 jadi 45000)
6. Klik "Simpan"
7. Data otomatis terupdate di semua halaman

Kasus 4: Hapus Transaksi Ganda
1. Buka halaman "Data"
2. Pilih transaksi yang double/duplikat
3. Klik tombol "Hapus"
4. Konfirmasi "Ya"
5. Data terhapus dari semua halaman dan file CSV

E.STRUKTUR DATA & FORMAT FILE
1. Format CSV
   id,date,description,amount,category,emotion,notes
   TRX2512240208001,2025-12-24,Kopi Starbucks,50000.00,Makanan & Minuman,Senang,Meeting dengan klien
   TRX2512240208002,2025-12-24,Bensin,100000.00,Transportasi,Netral,Isi bensin mobil
2. Kategori yang Didukung
   1. Senang
   2. Sedih
   3. Stres
   4. Marah
   5. Netral
   3. Emosi yang Didukung

F.BATASAN APLIKASI
Maksimal Data: Tidak ada batasan teknis (tergantung memory)
File Size: CSV file bisa besar, tapi tetap efisien
Multi-user: Tidak support (single user application)
Network: Tidak ada fitur cloud sync (local only)
Backup: Manual backup (copy file CSV)