#!/bin/bash

# 🚀 Commandes Essentielles - Reclamation Notes IBAM

echo "╔════════════════════════════════════════════════════╗"
echo "║  📖 COMMANDES ESSENTIELLES RECLAMATION             ║"
echo "╚════════════════════════════════════════════════════╝"
echo ""

# Menu
PS3='Sélectionnez une commande: '
options=(
    "🐳 Démarrer avec Docker Compose"
    "🔨 Build backend (Maven)"
    "⚛️  Build frontend (npm)"
    "🧪 Lancer les tests"
    "📊 Vérifier la santé de l'app"
    "🔄 Redémarrer les services"
    "🛑 Arrêter tous les services"
    "🧹 Nettoyer (rm containers, images, volumes)"
    "📱 Afficher les logs"
    "📚 Afficher la documentation"
    "❌ Quitter"
)

select opt in "${options[@]}"
do
    case $opt in
        "🐳 Démarrer avec Docker Compose")
            echo ""
            echo "🚀 Démarrage de l'application..."
            docker-compose up -d
            echo ""
            echo "✅ Services démarrés:"
            echo "   - PostgreSQL: localhost:5432"
            echo "   - Backend API: http://localhost:8080"
            echo "   - Frontend: http://localhost:3000"
            echo ""
            break
            ;;
        "🔨 Build backend (Maven)")
            echo ""
            echo "🔨 Build du backend..."
            cd reclamation
            ./mvnw clean package -DskipTests
            cd ..
            echo "✅ Build terminé"
            echo ""
            break
            ;;
        "⚛️  Build frontend (npm)")
            echo ""
            echo "⚛️  Build du frontend..."
            cd frontend
            npm install
            npm run build
            cd ..
            echo "✅ Build terminé"
            echo ""
            break
            ;;
        "🧪 Lancer les tests")
            echo ""
            echo "🧪 Tests disponibles:"
            read -p "Choisir: (1)Unittest (2)Selenium (3)Charge (4)Performance: " test_choice
            case $test_choice in
                1)
                    cd reclamation
                    ./mvnw test
                    cd ..
                    ;;
                2)
                    cd tests
                    python -m pytest exercice1_test_fonctionnel.py -v
                    cd ..
                    ;;
                3)
                    cd tests
                    python exercice2_test_charge.py
                    cd ..
                    ;;
                4)
                    cd tests
                    python exercice3_test_performance.py
                    cd ..
                    ;;
                *)
                    echo "Option invalide"
                    ;;
            esac
            echo ""
            break
            ;;
        "📊 Vérifier la santé de l'app")
            echo ""
            echo "🔍 Vérification de la santé..."
            echo ""
            echo "Backend health check:"
            curl -s http://localhost:8080/actuator/health | jq . || curl -s http://localhost:8080/actuator/health
            echo ""
            echo "Frontend:"
            curl -I http://localhost:3000 | head -5
            echo ""
            break
            ;;
        "🔄 Redémarrer les services")
            echo ""
            echo "🔄 Redémarrage..."
            docker-compose restart
            echo "✅ Services redémarrés"
            echo ""
            break
            ;;
        "🛑 Arrêter tous les services")
            echo ""
            echo "🛑 Arrêt des services..."
            docker-compose down
            echo "✅ Services arrêtés"
            echo ""
            break
            ;;
        "🧹 Nettoyer (rm containers, images, volumes)")
            echo ""
            read -p "⚠️  Cela supprimera les données. Continuer? (y/N): " confirm
            if [ "$confirm" = "y" ]; then
                docker-compose down -v
                docker system prune -f
                echo "✅ Nettoyage terminé"
            else
                echo "Annulé"
            fi
            echo ""
            break
            ;;
        "📱 Afficher les logs")
            echo ""
            echo "Sélectionner le service:"
            read -p "Service: (1)api-backend (2)frontend (3)postgres (4)tous: " log_choice
            case $log_choice in
                1) docker-compose logs -f api-backend ;;
                2) docker-compose logs -f frontend ;;
                3) docker-compose logs -f postgres ;;
                4) docker-compose logs -f ;;
                *) echo "Option invalide" ;;
            esac
            echo ""
            break
            ;;
        "📚 Afficher la documentation")
            echo ""
            echo "📚 Documentation disponible:"
            echo ""
            echo "1. README.md"
            echo "   → Aperçu du projet"
            echo ""
            echo "2. DEPLOYMENT_GUIDE.md"
            echo "   → Guide déploiement DigitalOcean"
            echo ""
            echo "3. TESTING_GUIDE.md"
            echo "   → Guide tests complets"
            echo ""
            echo "4. IMPROVEMENTS_SUMMARY.md"
            echo "   → Résumé des améliorations"
            echo ""
            read -p "Afficher lequel? (1-4 ou 0 pour quitter): " doc_choice
            case $doc_choice in
                1) less README.md ;;
                2) less DEPLOYMENT_GUIDE.md ;;
                3) less TESTING_GUIDE.md ;;
                4) less IMPROVEMENTS_SUMMARY.md ;;
                0) echo "Annulé" ;;
                *) echo "Option invalide" ;;
            esac
            echo ""
            break
            ;;
        "❌ Quitter")
            echo ""
            echo "À bientôt! 👋"
            echo ""
            break
            ;;
        *) echo "Option invalide $REPLY" ;;
    esac
done

# Afficher les URLs utiles
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🔗 URLs Utiles:"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "🎨 Application: http://localhost:3000"
echo "📚 API Swagger: http://localhost:8080/swagger-ui.html"
echo "❤️  Health: http://localhost:8080/actuator/health"
echo "🗄️  PostgreSQL: localhost:5432"
echo ""
echo "📖 Documentation: Voir DEPLOYMENT_GUIDE.md"
echo ""
