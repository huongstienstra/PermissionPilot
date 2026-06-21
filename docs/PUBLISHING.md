# Publishing

PermissionPilot is configured for Maven publishing with these coordinates:

```text
io.github.huongstienstra.permissionpilot:permissionpilot-core:0.1.0
io.github.huongstienstra.permissionpilot:permissionpilot-compose:0.1.0
io.github.huongstienstra.permissionpilot:permissionpilot-test:0.1.0
```

## One-time Maven Central setup

1. Create a Central Portal account at `https://central.sonatype.com`.
2. Register or claim a namespace for `io.github.huongstienstra`.
   - Maven Central requires namespace ownership.
   - Use the GitHub repository verification flow for the `huongstienstra` account.
3. Generate a Central Portal user token.
4. Create a GPG key for artifact signing.
5. Export the private signing key for Gradle:

```bash
gpg --export-secret-keys --armor <key-id>
```

## Local credentials

Keep secrets out of git. Add them to `~/.gradle/gradle.properties`:

```properties
mavenCentralUsername=your-central-token-username
mavenCentralPassword=your-central-token-password
signingInMemoryKey=-----BEGIN PGP PRIVATE KEY BLOCK-----...
signingInMemoryKeyId=your-key-id
signingInMemoryKeyPassword=your-key-password
```

The Central username/password must come from a Central Portal user token, not your login password.

## Verify before publishing

```bash
ANDROID_HOME=$HOME/Library/Android/sdk ./gradlew check :sample:assembleDebugAndroidTest
ANDROID_HOME=$HOME/Library/Android/sdk ./gradlew :sample:connectedDebugAndroidTest
```

## Publish

Upload a release deployment:

```bash
ANDROID_HOME=$HOME/Library/Android/sdk ./gradlew publishToMavenCentral
```

Then open Central Portal deployments and publish the deployment.

For automatic release after upload:

```bash
ANDROID_HOME=$HOME/Library/Android/sdk ./gradlew publishAndReleaseToMavenCentral
```

## GitHub Actions

The repository includes a manual `Publish` workflow. Add these repository secrets before running it:

```text
MAVEN_CENTRAL_USERNAME
MAVEN_CENTRAL_PASSWORD
SIGNING_IN_MEMORY_KEY
SIGNING_IN_MEMORY_KEY_ID
SIGNING_IN_MEMORY_KEY_PASSWORD
```

Run the workflow from GitHub Actions. Leave `release=false` to upload and review the deployment in Central Portal, or set `release=true` to publish automatically after upload.

## Consumer usage after release

```kotlin
dependencies {
    implementation("io.github.huongstienstra.permissionpilot:permissionpilot-core:0.1.0")
    implementation("io.github.huongstienstra.permissionpilot:permissionpilot-compose:0.1.0")
    testImplementation("io.github.huongstienstra.permissionpilot:permissionpilot-test:0.1.0")
}
```
