#!/bin/bash

# 🚀 Script de Setup Complet du Projet Reclamation

set -e  # Exit on error

echo "╔════════════════════════════════════════════╗"
echo "║  🚀 SETUP RECLAMATION NOTES - IBAM         ║"
echo "╚════════════════════════════════════════════╝"
echo ""

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check prerequisites
echo "📋 Vérification des prérequis..."

if ! command -v java &> /dev/null; then
    echo -e "${RED}❌ Java n'est pas installé${NC}"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | grep -oP '(?<=version ")[\d.]+(?=")' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 21 ]; then
    echo -e "${RED}❌ Java 21+ requis (version actuelle: $JAVA_VERSION)${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Java $(java -version 2>&1 | grep -oP '(?<=version ")[\d.]+(?=")'${NC}"

if ! command -v node &> /dev/null; then
    echo -e "${RED}❌ Node.js n'est pas installé${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Node.js $(node --version)${NC}"

if ! command -v npm &> /dev/null; then
    echo -e "${RED}❌ npm n'est pas installé${NC}"
    exit 1
fi
echo -e "${GREEN}✅ npm $(npm --version)${NC}"

if ! command -v psql &> /dev/null; then
    echo -e "${YELLOW}⚠️  PostgreSQL client pas trouvé (mais peut être sur Docker)${NC}"
fi

if ! command -v docker &> /dev/null; then
    echo -e "${YELLOW}⚠️  Docker pas trouvé (Docker Compose ne fonctionnera pas)${NC}"
fi

echo ""
echo "📦 Installation des dépendances..."
echo ""

# Backend setup
echo "🔧 Backend Spring Boot..."
cd reclamation
if [ ! -f "src/main/resources/application.yml" ]; then
    if [ -f "src/main/resources/application-example.yml" ]; then
        cp src/main/resources/application-example.yml src/main/resources/application.yml
        echo -e "${GREEN}✅ Fichier application.yml créé${NC}"
    fi
fi

echo "   📥 Téléchargement des dépendances Maven..."
./mvnw clean install -DskipTests -q 2>/dev/null || mvn clean install -DskipTests -q

echo -e "${GREEN}✅ Backend configuré${NC}"
cd ..

# Frontend setup
echo ""
echo "⚛️  Frontend React..."
cd frontend
echo "   📥 Installation des dépendances npm..."
npm install -q
echo -e "${GREEN}✅ Frontend configuré${NC}"
cd ..

# Tests dependencies
echo ""
echo "🧪 Tests..."
cd tests
echo "   📥 Installation des dépendances Python..."
if [ -f "requirements.txt" ]; then
    pip install -q -r requirements.txt
else
    pip install -q selenium requests pytest
fi
echo -e "${GREEN}✅ Tests configurés${NC}"
cd ..

# Environment setup
echo ""
echo "⚙️  Configuration de l'environnement..."
if [ ! -f ".env" ]; then
    cp .env.example .env
    echo -e "${GREEN}✅ Fichier .env créé (modifiez-le si nécessaire)${NC}"
else
    echo -e "${GREEN}✅ Fichier .env existe déjà${NC}"
fi

echo ""
echo "╔════════════════════════════════════════════╗"
echo "║  ✅ SETUP TERMINÉ AVEC SUCCÈS              ║"
echo "╚════════════════════════════════════════════╝"
echo ""
echo "🎯 Prochaines étapes:"
echo ""
echo "  1️⃣  Lancez la base de données:"
echo "     docker-compose up -d postgres"
echo ""
echo "  2️⃣  Lancez l'application complète:"
echo "     docker-compose up"
echo ""
echo "  3️⃣  Ou lancez manuellement:"
echo "     Terminal 1 (Backend):"
echo "       cd reclamation && ./mvnw spring-boot:run"
echo ""
echo "     Terminal 2 (Frontend):"
echo "       cd frontend && npm run dev"
echo ""
echo "  4️⃣  Ouvrez dans le navigateur:"
echo "     http://localhost:3000"
echo ""
echo "📚 Documentation:"
echo "   - Déploiement: DEPLOYMENT_GUIDE.md"
echo "   - Tests: TESTING_GUIDE.md"
echo "   - Projet: README.md"
echo ""
