import Foundation
import GoogleSignIn
import Shared

class GoogleAuthCoordinator {
    static let shared = GoogleAuthCoordinator()

    // Web Application Client ID from Google Cloud Console
    private let serverClientID = "5375786843-13lst1ihkjl1gh1nduhb5ijd7fnuutrm.apps.googleusercontent.com"

    func setupListeners() {
        NotificationCenter.default.addObserver(forName: NSNotification.Name("TriggerNativeIOSGoogleAuth"), object: nil, queue: .main) { _ in
            self.signIn()
        }
        NotificationCenter.default.addObserver(forName: NSNotification.Name("TriggerNativeIOSGoogleLogout"), object: nil, queue: .main) { _ in
            GIDSignIn.sharedInstance.signOut()
        }
    }

    private func signIn() {
        guard let rootViewController = UIApplication.shared.windows.first?.rootViewController else {
            GoogleAuthEngine.companion.onAuthFailed(errorMsg: "Missing root container context.")
            return
        }

        let config = GIDConfiguration(clientID: serverClientID)
        GIDSignIn.sharedInstance.configuration = config

        GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController) { result, error in
            if let error = error {
                GoogleAuthEngine.companion.onAuthFailed(errorMsg: error.localizedDescription)
                return
            }

            guard let idToken = result?.user.idToken?.tokenString else {
                GoogleAuthEngine.companion.onAuthFailed(errorMsg: "Identity token missing from Apple authentication chain.")
                return
            }

            // Forward the secure iOS client token directly into your common Kotlin Multiplatform engine
            GoogleAuthEngine.companion.onTokenReceived(idToken: idToken)
        }
    }
}
