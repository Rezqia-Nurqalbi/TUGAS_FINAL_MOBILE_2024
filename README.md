# SPOTLIGHTNEWS
SpotlightNews merupakan aplikasi yang dibuat untuk penyuka berita. Dengan SpotlightNews, kamu dapat menemukan berbagai kategori berita seperti General, Business, Entertainment, Health, Science, Sports, dan Technology. Berita tersedia dari lima negara yaitu India, Indonesia, Canada, United States, dan United Kingdom.

# FITUR UTAMA
• Tampilan Berita: SpotlightNews menyediakan berbagai berita yang diinginkan.

• Kategori Berita: SpotlightNews memiliki berbagai kategori berita yaitu General, Business, Entertainment, Health, Science, Sports, dan Technology.

• Negara: Berita tersedia dari lima negara yaitu India, Indonesia, Canada, United States, dan United Kingdom.

• Detail Berita: Menampilkan informasi lengkap dari berita yaitu source, author, title, gambar, deskripsi, tanggal publikasi, konten, dan tombol untuk membuka situs berita.

• Pencarian: Fitur untuk menemukan berita sesuai judul yang diinginkan.

• Riwayat: Fitur yang menyimpan data berita yang telah dibuka, mencatat tanggal dan waktu terakhir dibuka, dan memperbarui saat dibuka kembali.

# Cara Penggunaan Aplikasi
1. Memulai Aplikasi
    •	Saat aplikasi pertama kali dibuka, akan menampilkan SampulActivity selama beberapa detik sebelum masuk ke MainActivity.
   
2. Navigasi Utama
   
    •	Di layar utama, terdapat tiga opsi navigasi di bagian bawah: News, Search, dan History.


   # News (Berita)
   
    •	Nav ini adalah tampilan default yang memuat berita utama dari berbagai kategori dan negara.
   
    •	Sementara menunggu data berita, akan muncul progress bar untuk menunjukkan bahwa data sedang diambil dari API.
   
    •	Di nav ini, kita dapat memilih berita dan mengklik item berita untuk melihat detailnya.
   
    •	Halaman ini menampilkan berita dalam bentuk gambar, judul, dan tanggal publikasi.
   
    •	Kita dapat memilih berita berdasarkan negara dan kategori menggunakan spinner (dropdown) di bagian atas daftar berita.
   
    •	Negara yang tersedia: India, Indonesia, Canada, United States, dan United Kingdom.
   
    •	Kategori yang tersedia: General, Business, Entertainment, Health, Science, Sports, dan Technology.
   
    •	Setelah memilih negara dan kategori, daftar berita akan diperbarui sesuai pilihan.
   
    •	Jika tidak ada koneksi internet atau terjadi kesalahan, akan muncul pesan kesalahan dan tombol "Retry".
   

   # Detail Berita
   
    •	Mengklik card di halaman pertama akan mengarahkan ke halaman detail berita. Halaman ini menampilkan source, author, title, deskripsi, url berita, gambar, tanggal publikasi, dan konten berita.
   
    •	Setelah melihat detail berita, akan otomatis masuk ke History fragment.

   # Search (Pencarian)
   
    •	SpotlightNews menyediakan fitur pencarian untuk mencari berita berdasarkan judul.
   
    •	Saat mencari data berita, akan muncul progress bar selama data belum muncul.
   
    •	Daftar hasil pencarian akan ditampilkan dan dapat mengklik card berita untuk melihat detailnya.
   
    •	Setelah mengklik detail berita, akan langsung masuk ke riwayatnya.
   
    •	Jika tidak ada koneksi internet, akan muncul pesan kesalahan.
   
    •	Jika tidak ada data yang cocok, akan muncul pesan "No data".

   # History (Riwayat)
   
    •	Fitur riwayat menampilkan daftar berita yang telah dibuka sebelumnya.
   
    •	Menyimpan tanggal dan waktu terakhir dibuka.
   
    •	Ketika dibuka di riwayat, tanggal dan waktu terakhir dibuka akan diperbarui.
   
    •	Riwayat berita disimpan dalam basis data lokal pada perangkat.

# Implementasi Teknis
• Intent

• Activity

• RecyclerView

• Fragment & Navigation

• Background Thread

• Networking

• SQLite
