# Guru Maarif DH (WebView)

Aplikasi Android webview dengan whitelist beberapa subdomain dan **Swipe to Refresh**.

## Paket
- `id.maarifdh.guru`

## Whitelist (dibuka di dalam app)
- `guru.maarifdh.sch.id`
- `absensi.maarifdh.sch.id`
- `kaldik.maarifdh.sch.id`
- `jadwal.maarifdh.sch.id`
- `krisar.maarifdh.sch.id`

Selain host di atas akan otomatis dibuka di browser default.

## Build cepat (Android Studio)
1. Open Android Studio → *Open an Existing Project* → pilih folder ini.
2. Tunggu sync selesai → **Build > Build APK(s)**.
3. APK ada di `app/build/outputs/apk/debug/app-debug.apk`.

> Catatan: Android Studio bisa otomatis membuat/upgrade Gradle wrapper.
> Icon terdeteksi dari: ./.local/lib/python3.11/site-packages/nbclassic/static/base/images/logo.png

## Modifikasi tambahan (opsional)
- Ganti landing page di `MainActivity.kt` (`HOME_URL`).
- Ubah warna background ikon di `res/values/colors.xml` (`ic_launcher_background`).
- Tambah host baru cukup edit set `ALLOWED_HOSTS` di `MainActivity.kt`.
