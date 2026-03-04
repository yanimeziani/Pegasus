# Release Pegasus APK

Version is set to **0.3.0** (versionCode 12). To publish an APK to GitHub Releases:

## Option A — Push a tag (recommended)

From your **repo root** (parent of `pegasus/`), after committing any changes:

```bash
git add -A && git commit -m "Release Pegasus 0.3.0"   # if you have changes
git tag v0.3.0
git push origin main
git push origin v0.3.0
```

The workflow **Pegasus APK — GitHub Release** (`.github/workflows/pegasus-release.yml`) will run, build the release APK, and create a GitHub Release with the APK attached.

## Option B — Run workflow manually

1. In GitHub: **Actions** → **Pegasus APK — GitHub Release**
2. Click **Run workflow** → **Run workflow**
3. When it finishes, open **Releases** and download the new APK (or the one for the latest tag).

## After release

- Release will be **prerelease** and the asset will be named like `pegasus-v0.3.0-arm64.apk`.
- For the next release, bump `versionCode` and `versionName` in `pegasus/app/build.gradle.kts`, then use a new tag (e.g. `v0.3.1`).
