# Release Pegasus APK

Current app version: **0.3.2** (versionCode 14). To publish an APK to GitHub Releases:

## Option A — Push a tag (recommended)

From your **repo root** (parent of `pegasus/`), after committing any changes:

```bash
git add -A && git commit -m "Release Pegasus 0.3.2"   # if you have changes
git tag v0.3.2
git push origin main
git push origin v0.3.2
```

The workflow **Pegasus APK — GitHub Release** (root `.github/workflows/pegasus-release.yml`) will run, build the release APK, and create a GitHub Release with the APK attached.

## Option B — Run workflow manually

1. In GitHub: **Actions** → **Pegasus APK — GitHub Release**
2. Click **Run workflow**
3. Optionally set **version** (e.g. `v0.3.2`). Default is `v0.3.2`; use a new tag if the tag already exists (e.g. `v0.3.3`).
4. Click **Run workflow**
5. When it finishes, open **Releases** and download the APK (e.g. `pegasus-v0.3.2-arm64.apk`).

## After release

- Release is created as **prerelease** with asset `pegasus-<version>-arm64.apk`.
- For the next release, bump `versionCode` and `versionName` in `pegasus/app/build.gradle.kts`, then push a new tag or run the workflow with that version.
